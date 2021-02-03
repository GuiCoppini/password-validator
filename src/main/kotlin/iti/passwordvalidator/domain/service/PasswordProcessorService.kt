package iti.passwordvalidator.domain.service

import iti.passwordvalidator.common.logging.LogUtils.getLogger
import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.model.Constraint.DIGITS_COUNT
import iti.passwordvalidator.domain.model.Constraint.INVALID_CHARACTER
import iti.passwordvalidator.domain.model.Constraint.LOWER_CASE_LETTER_COUNT
import iti.passwordvalidator.domain.model.Constraint.MINIMUM_SIZE
import iti.passwordvalidator.domain.model.Constraint.NO_REPEATING_CHARACTER
import iti.passwordvalidator.domain.model.Constraint.SPECIAL_CHARACTER
import iti.passwordvalidator.domain.model.Constraint.UPPER_CASE_LETTER_COUNT
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


// fiz um service implementando uma interface porque a ideia eh que quem use esse service confie na INTERFACE
// que foi declarada. Um service pode mudar quando quiser, uma interface dificilmente quebra o contrato
interface PasswordProcessorService {
    // fornece um metodo que pode ser usado tanto como uma validacao customizada (passando os criterios)
    // ou como o padrao que vai utilizar a regra de negocio da aplicacao para validar (no caso, todos os criterios)

    // no nosso caso, a interface so vai utilizar o padrao
    fun process(password: String, analyzedConstraints: List<Constraint>? = null): Boolean
}


// na mesma classe da interface pois so tem essa implementacao
// caso surjam outras, a ideia eh passar para outros arquivos, mas aqui estou seguindo o conceito do
// YAGNI (You aren't gonna need it)
@Service
class PasswordProcessorServiceImpl(val factory: ValidatorFactory) : PasswordProcessorService {
    val log = getLogger(this::class.java)

    private val defaultCriteria = listOf(
            MINIMUM_SIZE,
            DIGITS_COUNT,
            LOWER_CASE_LETTER_COUNT,
            UPPER_CASE_LETTER_COUNT,
            SPECIAL_CHARACTER,
            INVALID_CHARACTER,
            NO_REPEATING_CHARACTER
    )

    @Cacheable("password")
    override fun process(password: String, analyzedConstraints: List<Constraint>?): Boolean {
        // se nao passar nenhum criterio, usa todos definidos como padrao

        val result = if (analyzedConstraints.isNullOrEmpty()) {
            log.info("No analyzed criteria informed, using default criteria. criteria=$defaultCriteria")
            password.validateWith(defaultCriteria)
        } else {
            log.info("Using informed criteria. criteria=$defaultCriteria")
            password.validateWith(analyzedConstraints)
        }

        log.info("Finished validating password. finalResult=$result")
        return result
    }

    // codigo "core" da API
    // chama o validator de cada criterio
    // se nao contiver false na lista, deu tudo valido

    // loggar aqui evita precisar loggar cada criterio individualmente
    fun String.validateWith(constraints: List<Constraint>) =
            !constraints.map { constraint ->
                log.info("Getting validator for constraint=$constraint")
                val validator = factory.getValidatorFor(constraint)


                val valid = validator.validate(this)
                log.info("Validated password with validator=${validator::class.simpleName} constraint=$constraint, result=$valid")
                valid
            }.contains(false)
}