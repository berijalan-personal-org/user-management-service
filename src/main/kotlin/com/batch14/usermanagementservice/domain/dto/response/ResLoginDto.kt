package com.batch14.usermanagementservice.domain.dto.response

import java.io.Serializable

data class ResLoginDto(
    val token: String?,
    val otp: String? = null
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 4442938784686322472L
    }
}
