package com.batch14.usermanagementservice.service

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto

interface MasterUserService {
    fun findAllActiveUsers(): List<ResGetAllUserDto>
    fun findUserById(id: Int): ResGetAllUserDto?
    fun registerUser(reqRegisterUserDto: ReqRegisterUserDto): ResGetAllUserDto
    fun validateOtp(username: String, otp: String): ResLoginDto
    fun login(req: ReqLoginDto): ResLoginDto
    fun updateUser(reqUpdateUserDto: ReqUpdateUserDto, id: Int): ResGetAllUserDto
    fun softDelete(id: Int)
    fun hardDelete(id: Int)
    fun findAllUsersByIds(ids: List<Int>): List<ResGetAllUserDto>
}