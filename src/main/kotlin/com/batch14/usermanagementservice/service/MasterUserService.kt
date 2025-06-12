package com.batch14.usermanagementservice.service

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto

interface MasterUserService {
    fun findAllActiveUsers(): List<ResGetAllUserDto>
    fun findUserById(id: Int): ResGetAllUserDto?
    fun registerUser(reqRegisterUserDto: ReqRegisterUserDto): ResGetAllUserDto
    fun login(req: ReqLoginDto): ResLoginDto
}