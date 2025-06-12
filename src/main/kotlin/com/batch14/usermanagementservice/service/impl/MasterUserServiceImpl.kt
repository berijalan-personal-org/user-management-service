package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import com.batch14.usermanagementservice.exceptions.CustomException
import com.batch14.usermanagementservice.repository.MasterRoleRepository
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.MasterUserService
import com.batch14.usermanagementservice.util.BCryptUtil
import com.batch14.usermanagementservice.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val bCrypt: BCryptUtil,
    private val jwtUtil: JwtUtil
): MasterUserService{
    override fun findAllActiveUsers(): List<ResGetAllUserDto> {
        val rawData = masterUserRepository.getAllActiveUser()
        val result = mutableListOf<ResGetAllUserDto>()
        rawData.forEach{ u ->
            result.add(
                ResGetAllUserDto(
                    username = u.username,
                    id = u.id,
                    email = u.email,
                    // jika user memiliki role maka ambil id_role, ingat karena entitiy nullable
                    // jika tidak ada role maka value null
                    roleId = u.role?.id,
                    // jika user memiliki role maka ambil nama role, ingat karena entitiy nullable
                    roleName = u.role?.name
                    // menyebabkan n+1 query problem, sehingga disarankan pakai INNER JOIN
                )
            )
        }
        return result
    }

    override fun findUserById(id: Int): ResGetAllUserDto? {
        val user = masterUserRepository.getUserById(id)
        return user?.let {
            ResGetAllUserDto(
                username = it.username,
                id = it.id,
                email = it.email,
                roleId = it.role?.id,
                roleName = it.role?.name
            )
        }
    }

    override fun registerUser(reqRegisterUserDto: ReqRegisterUserDto): ResGetAllUserDto {
        val role = if (reqRegisterUserDto.roleId != null) {
            masterRoleRepository.findById(reqRegisterUserDto.roleId)
        } else {
            Optional.empty() // beda dengan null
        }

        val existingUserEmail = masterUserRepository.findOneByEmail(reqRegisterUserDto.email)
        if (existingUserEmail != null) {
            throw CustomException(
                "Email ${reqRegisterUserDto.email} sudah terdaftar",
                HttpStatus.BAD_REQUEST.value(),
            )
        }

        val existingUserUsername = masterUserRepository.findOneByUsername(reqRegisterUserDto.username)
        if (existingUserUsername.isPresent) {
            throw CustomException(
                "Username ${reqRegisterUserDto.username} sudah terdaftar",
                HttpStatus.BAD_REQUEST.value(),
            )
        }

        val userRaw = MasterUserEntity(
            email = reqRegisterUserDto.email,
            username = reqRegisterUserDto.username,
            password = reqRegisterUserDto.password,
            role = if (role.isPresent) {
                role.get()
            } else {
                null
            }
        )

        val user = masterUserRepository.save(userRaw)
        return ResGetAllUserDto(
            username = user.username,
            id = user.id,
            email = user.email,
            roleId = user.role?.id,
            roleName = user.role?.name
        )
    }

    override fun login(req: ReqLoginDto): ResLoginDto {
        val userEntityOpt = masterUserRepository.findOneByUsername(req.username)
        if (userEntityOpt.isEmpty) {
            throw CustomException(
                "Username atau password salah",
                HttpStatus.NOT_FOUND.value(),
            )
        }

        val userEntity = userEntityOpt.get()

        if (!bCrypt.verify(req.password, userEntity.password)) {
            throw CustomException(
                "Username atau password salah",
                HttpStatus.NOT_FOUND.value(),
            )
        }

        val role = if (userEntity.role != null) {
            userEntity.role!!.name
        } else {
            "user"
        }
        val token = jwtUtil.generateToken(userEntity.id, role)
        return ResLoginDto(token)
    }
}