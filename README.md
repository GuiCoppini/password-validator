# Password-Validator
Password-Validator é uma aplicação escrita em Kotlin + SpringBoot e é capaz de validar uma senha com base em alguns
critérios pré-definidos, chamados de "Constraint" na aplicalção.

### Índice
* [Frameworks e dependências](#frameworks-e-dependências)



## Frameworks e dependências
A aplicação, como dito anteriormente foi escrita em Kotlin + SpringBoot, a ferramenta de build utilizada foi o Gradle.

A aplicação também utiliza:  
* JUnit - testes unitários
* MockK - ferramenta utilizada para mockar services utilizados pelos services em teste
* Swagger - documentação (será abordado mais à frente)
* Sleuth - Utilizado para facilitar tracking de chamadas à API com traceID e spanID, seria muito mais vantajoso caso 
estivesse em um ecossistema de aplicações onde outros sistemas também utilizassem Sleuth


## Como rodar a aplicação
Para rodar a aplicação é simples, basta se localizar na pasta raiz do projeto e executar no terminal:
```bash
./gradlew bootRun
```

## Estrutura da aplicação
A aplicação foi divida principalmente em dois pacotes, o `rest` e o `domain` (também tem o common, mas não é tão "principal" assim).
* rest - contém tudo que diz respeito à camada REST, como classes de request/response, controllers etc;
* domain - contém o `core` da aplicação, como exceptions de sistema e services, que agrupam as regras de negócio;
* common - contém tudo que é utilizado em ambos os pacotes, nesse caso, um pedaço de código relacionado a log, pois não é uma lógica única de uma camada ou de outra.  
  
A ideia de separar as camadas de tal maneira é para que a camada de aplicação seja independente da camada de interface.
Com isso podemos utilizar a lógica da aplicação e mudar o ponto de entrada, como por exemplo deixar de usar REST e passar a utilizar alguma CLI, ou até mesmo outra tecnologia como gRPC, pois em teoria só deveria ser reimplementada a lógica da interface.  
Por outro lado, a camada REST conhece alguns objetos da camada de service, pois a camada da interface é quem faz a comunicação do mundo de fora (cliente) com o mundo de dentro (aplicação), mas a aplicação (domain) não deve conhecer entidades do mundo "de fora".

#### E o banco?
Como a aplicação faz um processamente extremamente leve, não foi necessário um banco de dados, o máximo que a aplicação tem nesse sentido
é um @Cacheable padrão no service que permite que a aplicação deixe a resposta booleana cacheada por alguns minutos.

A escolha da aplicação ser sem banco foi tomada levando em conta que o tempo de processamento de uma String, na imensa maioria das vezes,
seria muito mais eficiente do que um I/O para buscar uma senha no banco e ver se já foi computada.  
No entando, caso fosse necessário persistir isso de alguma maneira,
o ideal seria utilizar algum Hash sobre a senha para que ela não ficasse em plaintext no banco,
assim como fiz na hora de loggar.  
E sobre a escolha do banco, como não há JOINs, UPDATEs e DELETEs e as únicas operações que seriam executada são INSERTs e SELECTs, 
o melhor candidato que temos é algum banco não-relacional, como MongoDB, por exemplo.  

## Como funciona a aplicação?
Com a estrutura da aplicação explicada, vamos ao funcionamento da aplicação.
Dentro da aplicação, cada critério de validação foi chamado como Constraint, é um enum indicando cada um dos passos pertencentes à validação da senha, como:
* `MINIMUM_SIZE`
* `DIGITS_COUNT`
* `LOWER_CASE_LETTER_COUNT`
* `UPPER_CASE_LETTER_COUNT`
* `SPECIAL_CHARACTER`
* `INVALID_CHARACTER`
* `NO_REPEATING_CHARACTER`  
Cada um destes Constraints está atrelado a um Service que implementa a interface Validator.  

##### Constraints e Services
Como há diversas restrições de senha, dividí-las em classes num padrão próximo do Strategy nos traz algumas vantagens:
* Facilidade para criar Constraints novas, basta criar o enum e o Validator
* Facilidade para EDITAR Constraints existentes, basta abrir a implementação do Validator
* Facilidade para testar cada validação independentemente
Como desvantagens, temos que foram criadas N classes, uma para cada constraint, mas ainda há um ganho, pois no contrário
teríamos uma classe agrupando todas as validações, o que dificultaria testes, leitura e a manutenção a longo prazo.

##### Factory
No caso de uma adição ou remoção de constraint existente, não precisamos alterar dezenas de trechos de código,
basta criar a constraint e implementar a interface e, no máximo, colocar a constraint como uma das Constraints padrão da aplicação (será explicado logo menos).  
Essa flexibilidade é trazida pelo ValidatorFactory, que se assemelha ao Factory Method, onde você passa uma Constraint para ele, e o Service
se responsabiliza por encontrar a implementação de Validator relacionado àquela Constraint.

##### Constraints padrão
Quando construí a parte de validação, achei interessante expor um método que permite que o cliente escolha quais critérios de 
validação ele vai usar; então num futuro hipotético, podemos resolver validar senha com base nos critérios enviados via API REST.  
No entando, essa interface foi criada porém não foi usada ainda, pois quem consome a API REST sempre vai ser validado sob as Constraints padrão, definidas 
na PasswordProcessorService, Service que faz o orquestramento entre pegar o validator da Factory e chamar seu método de validação

TODO: Colocar diagrama aqui

##### RestExceptionHandler
Há uma classe RestExceptionHandler que é basicamente um @ControllerAdvice que tratará as exceptions geradas pela aplicação.
A ideia dessa classe é loggar a exception recebida e retornar o payload de erro necessário. Fiz essa camada para evitar 
retornar a mensagem crua da exception direto para o cliente, evitando possíveis falhas de segurança e mostrando código/classes e pacotes para quem
não deveria ter acesso. 

## Testes
No projeto, foram implementados testes unitários (em Services) e testes integrados (na interface).
Como a aplicação não possui banco, não foi necessário um Banco Embedded ou um TestContainer para realizar os testes integrados.

## Logs
Como disse anteriormente, a aplicação conta com o Sleuth, uma dependência que fornece, entre outras coisas,
IDs de tracing nos logs especificamente para sistemas distribuídos.
Os logs estão espalhados pelo sistema e indicam os critérios/constraints utilizados na validação da senha 
e quais Validators foram utilizados para cada constraint, facilitando a visualização do fluxo como um todo via log.

#### Coverage
A covertura de testes está acima de 90%, podemos ver isso pelo relatório gerado pelo JaCoCo:
```
./gradlew clean build jacocoTestReport
```
O HTML com os resultados dos testes ficarão no diretório `build/reports/jacoco/test/html/index.html` 
