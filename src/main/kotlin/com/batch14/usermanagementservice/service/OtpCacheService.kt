package com.batch14.usermanagementservice.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

interface OtpCacheService {
    fun cacheOtp(username: String, otp: String): String
    fun getCachedOtp(username: String): String?
    fun evictOtp(username: String)
}