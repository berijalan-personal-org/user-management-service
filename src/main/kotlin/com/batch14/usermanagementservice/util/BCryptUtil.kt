package com.batch14.usermanagementservice.util

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BCryptUtil {
    fun hash(password: String?): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    fun verify(password: String?, hashedPassword: String?): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}