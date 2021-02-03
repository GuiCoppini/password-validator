package iti.passwordvalidator.rest.payload

import io.swagger.annotations.ApiModelProperty

// objeto de response desacoplado do backend pois no futuro eh possivel adicionar/remover informacoes dele
data class ValidateResponse(
        @ApiModelProperty(value = "Boolean indicating if the password is valid based on pre-defined constraints")
        val valid: Boolean
)


data class ValidateRequest(
        @ApiModelProperty(value = "Password that will be validated",
                example = "MyPassword!!123")
        val password: String
)