package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.exceptions.GlobalExceptionHandler
import com.batch14.usermanagementservice.service.MasterUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController (
    private val masterUserService: MasterUserService
){
    @GetMapping("/active")
    fun getAllActiveUser(): ResponseEntity<BaseResponse<List<ResGetAllUserDto>>>{
        val users = masterUserService.findAllActiveUsers()
        return ResponseEntity.ok(
            BaseResponse(
                data = users
            )
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

    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody reqRegisterUserDto: ReqRegisterUserDto
    ): ResponseEntity<BaseResponse<ResGetAllUserDto>> {
        val user = masterUserService.registerUser(reqRegisterUserDto)

        return ResponseEntity(
            BaseResponse(
                data = user,
                message = "Register Success"
            ),
            HttpStatus.CREATED
        )
    }
}