package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.exceptions.GlobalExceptionHandler
import com.batch14.usermanagementservice.service.MasterUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    fun getUserById(@PathVariable id: Int): ResponseEntity<BaseResponse<ResGetAllUserDto>> {
        val user = masterUserService.findUserById(id)
            return ResponseEntity.ok(
                BaseResponse(
                    data = user
                )
            )
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

    @PostMapping("/login")
    fun login(
        @RequestBody req: ReqLoginDto
    ): ResponseEntity<BaseResponse<ResLoginDto>> {
        return ResponseEntity(
            BaseResponse(
                data = masterUserService.login(req),
                message = "Login Berhasil"
            ),
            HttpStatus.OK
        )
    }

    @PutMapping
    fun updateUser(
        @RequestBody reqUpdateUserDto: ReqUpdateUserDto
    ): ResponseEntity<BaseResponse<ResGetAllUserDto>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.updateUser(reqUpdateUserDto)
            )
        )
    }
}