package iti.passwordvalidator.rest

import io.swagger.annotations.ApiOperation
import iti.passwordvalidator.common.logging.LogUtils
import iti.passwordvalidator.common.logging.LogUtils.getLogger
import iti.passwordvalidator.domain.service.PasswordProcessorService
import iti.passwordvalidator.rest.payload.ValidateRequest
import iti.passwordvalidator.rest.payload.ValidateResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

// controller que tera tudo relacionado a senhas
@RequestMapping("/password")
@RestController
class PasswordController(val service: PasswordProcessorService) {
    val log = getLogger(this::class.java)

    @PostMapping("/validate")
    @ResponseBody
    @ApiOperation("Validates a password based on pre-defined constraints")
    fun validatePassword(@RequestBody request: ValidateRequest): ValidateResponse {
        val password = request.password

        log.info("Password validate request, passwordHash=${LogUtils.hash(password)}")
        // nao passar objeto REST para o service faz com que o service nao precise conhecer a interface que o utiliza
        val isValid = service.process(password)

        // body separado de response para colocar o que fizer
        // sentido para essa interface e para nao ser uma string pura
        return ValidateResponse(isValid)
    }
}