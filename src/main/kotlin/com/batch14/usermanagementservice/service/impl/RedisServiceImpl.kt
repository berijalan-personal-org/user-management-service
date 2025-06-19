package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.exceptions.CustomException
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.RedisService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.random.Random

@Service
class RedisServiceImpl(
    private val stringRedisTemplate: StringRedisTemplate,
    private val masterUserRepository: MasterUserRepository
): RedisService {
    override fun set(userId: Int): String {
        val user = masterUserRepository.findById(userId).orElseThrow {
            throw CustomException(
                "User dengan id $userId tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        val operationString = stringRedisTemplate.opsForValue()
        operationString.set("user:username:${user.username}", user.username, Duration.ofSeconds(600))
        return "User dengan username ${user.username} berhasil disimpan di Redis"
    }

    override fun get(userId: Int): String {
        val user = masterUserRepository.findById(userId).orElseThrow {
            throw CustomException(
                "User dengan id $userId tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        val operationString = stringRedisTemplate.opsForValue()

        val username = operationString.get("user:username:${user.username}")
            ?: throw CustomException(
                "User dengan username ${user.username} tidak ditemukan di Redis",
                HttpStatus.NOT_FOUND.value(),
            )
        return "User ID $userId dengan username $username berhasil diambil dari Redis"
    }

    override fun generateOtp(username: String): String {
        val otp = Random.nextInt(100000, 999999).toString()
        val operationString = stringRedisTemplate.opsForValue()
        operationString.set("otpCache:$username", otp, Duration.ofMinutes(15))
        println("Generated OTP for $username: $otp")
        return otp
    }

    override fun getOtp(username: String): String? {
        val operationString = stringRedisTemplate.opsForValue()
        return operationString.get("otpCache:$username")
    }

    @CacheEvict(value = ["otpCache"], key = "#userId")
    override fun evictOtp(userId: Int) {}

}
