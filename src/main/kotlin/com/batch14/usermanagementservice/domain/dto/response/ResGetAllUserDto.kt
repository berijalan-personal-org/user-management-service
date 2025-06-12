package com.batch14.usermanagementservice.domain.dto.response

data class ResGetAllUserDto(
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int?,
    var roleName: String? = null
)
