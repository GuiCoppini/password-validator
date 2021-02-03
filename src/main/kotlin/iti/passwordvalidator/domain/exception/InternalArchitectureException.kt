package iti.passwordvalidator.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// exception que so deve ocorrer quando o sistema realmente der algum erro nao tratavel
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class InternalArchitectureException(override val message: String?) : RuntimeException()