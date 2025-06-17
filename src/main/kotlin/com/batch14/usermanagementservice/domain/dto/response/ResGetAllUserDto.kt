package com.batch14.usermanagementservice.domain.dto.response

import java.io.Serializable

data class ResGetAllUserDto(
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int?,
    var roleName: String? = null
): Serializable {
    companion object {
        private const val serialVersionUID: Long = -1472173711295631202L
    }
}
