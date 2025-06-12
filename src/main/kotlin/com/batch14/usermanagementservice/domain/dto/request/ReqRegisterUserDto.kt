package com.batch14.usermanagementservice.domain.dto.request

import jakarta.validation.constraints.NotBlank

data class ReqRegisterUserDto(
    @field:NotBlank(message = "username is required")
    val username: String,

    @field:NotBlank(message = "email is required")
    val email: String,

    @field:NotBlank(message = "password is required")
    val password: String,
    val roleId: Int?
)
