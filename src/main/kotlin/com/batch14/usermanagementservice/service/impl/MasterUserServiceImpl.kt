package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.MasterUserService
import org.springframework.stereotype.Service

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository
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
}