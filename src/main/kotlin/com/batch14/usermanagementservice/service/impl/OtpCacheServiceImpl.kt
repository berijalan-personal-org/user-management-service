package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.service.OtpCacheService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class OtpCacheServiceImpl () : OtpCacheService {
    @CachePut(value = ["otpCache"], key = "#username")
    override fun cacheOtp(username: String, otp: String): String {
        return otp
    }

    @Cacheable(value = ["otpCache"], key = "#username")
    override fun getCachedOtp(username: String): String? {
        return null
    }

    @CacheEvict(value = ["otpCache"], key = "#username")
    override fun evictOtp(username: String) {
    }
}