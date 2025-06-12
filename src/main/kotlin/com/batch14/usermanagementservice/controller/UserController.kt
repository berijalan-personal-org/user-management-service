package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.service.MasterUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController (
    private val masterUserService: MasterUserService
){
    @GetMapping("/active")
    fun getAllActiveUser(): ResponseEntity<List<ResGetAllUserDto>>{
        return ResponseEntity.ok(
            masterUserService.findAllActiveUsers()
        )
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<ResGetAllUserDto> {
        val user = masterUserService.findUserById(id)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}