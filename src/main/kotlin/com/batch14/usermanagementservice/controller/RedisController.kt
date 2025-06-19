package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.request.ReqRedisUserDto
import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.service.RedisService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/redis")
class RedisController (
    private val redisService: RedisService
){
    @PostMapping("/set")
    fun setValue(
        @RequestBody req: ReqRedisUserDto
    ): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = redisService.set(req.userId)
            )
        )
    }

    @GetMapping("/get/{req}")
    fun getValue(
        @PathVariable userId: Int
    ): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = redisService.get(userId)
            )
        )
    }

}