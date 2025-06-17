package com.batch14.usermanagementservice.service

interface RedisService {
    fun set(userId: Int): String
    fun get(userId: Int): String
}