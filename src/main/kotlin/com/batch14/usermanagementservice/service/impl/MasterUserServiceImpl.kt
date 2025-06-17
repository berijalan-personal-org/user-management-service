package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.domain.constant.Constant
import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterUserDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import com.batch14.usermanagementservice.exceptions.CustomException
import com.batch14.usermanagementservice.repository.MasterRoleRepository
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.MasterUserService
import com.batch14.usermanagementservice.util.BCryptUtil
import com.batch14.usermanagementservice.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Optional


// service untuk handling exception, jangan di controller
@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val bCrypt: BCryptUtil,
    private val jwtUtil: JwtUtil,
    private val httpServletRequest: HttpServletRequest
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

    //kalau data belum ada, maka akan dibuat di redis
    //jika data ada, maka akan diambil dari redis
    @Cacheable(
        "getUserById",
        key = "{#id}"
    )
    override fun findUserById(id: Int): ResGetAllUserDto? {
        val user = masterUserRepository.getUserById(id)
        if (user == null) {
            throw CustomException(
                "User dengan id $id tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }

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

        val hashedPassword = bCrypt.hash(reqRegisterUserDto.password)
        val userRaw = MasterUserEntity(
            email = reqRegisterUserDto.email,
            username = reqRegisterUserDto.username,
            password = hashedPassword,
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

    //delete cache
    @CacheEvict(
        value = ["getUserById", "getAllActiveUser"], //menghapus semua cache tidak peduli cache
        key = "{#userId}",
        allEntries = true
    )
    override fun updateUser(
        reqUpdateUserDto: ReqUpdateUserDto,
        id: Int
    ): ResGetAllUserDto {
        val userId = httpServletRequest.getHeader(Constant.HEADER_USER_ID)

        // pakai or else throw untuk convert user yg tidak optional sehingga bisa langsung user.email
        val user = masterUserRepository.findById(userId.toInt()).orElseThrow {
            CustomException(
                "User dengan id $userId tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }

        var existingUser = masterUserRepository.findOneByUsername(reqUpdateUserDto.username)
        if (existingUser.isPresent) {
            if (existingUser.get().id != userId.toInt()) {
                throw CustomException(
                    "Username ${reqUpdateUserDto.username} sudah terdaftar",
                    HttpStatus.BAD_REQUEST.value(),
                )
            }
        }

        var existingEmail = masterUserRepository.findOneByEmail(reqUpdateUserDto.email)
        if (existingEmail != null) {
            if (existingEmail.id != userId.toInt()) {
                throw CustomException(
                    "Email ${reqUpdateUserDto.email} sudah terdaftar",
                    HttpStatus.BAD_REQUEST.value(),
                )
            }
        }

        user.email = reqUpdateUserDto.email.toString()
        user.username = reqUpdateUserDto.username.toString()
        user.updatedBy = userId.toInt()

        val updatedUser = masterUserRepository.save(user)

        return ResGetAllUserDto(
            username = updatedUser.username,
            id = updatedUser.id,
            email = updatedUser.email,
            roleId = updatedUser.role?.id,
            roleName = updatedUser.role?.name
        )
    }

    override fun softDelete(id: Int) {
        val idDeletedBy = httpServletRequest.getHeader(Constant.HEADER_USER_ID)
        println(idDeletedBy)
        val actor = masterUserRepository.findById(idDeletedBy.toInt()).orElseThrow {
            CustomException(
                "User dengan id $idDeletedBy tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        val user = masterUserRepository.findById(id).orElseThrow(){
            CustomException(
                "User dengan id $id tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        user.deletedBy = actor.id
        user.isDelete = true
        masterUserRepository.save(user)
    }

    override fun hardDelete(id: Int) {
        val id_deletedBy = httpServletRequest.getHeader(Constant.HEADER_USER_ID)
        val actor = masterUserRepository.findById(id_deletedBy.toInt()).orElseThrow {
            CustomException(
                "User dengan id $id_deletedBy tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        println(actor.role?.name)
        if(actor.role?.name != "admin") {
            throw CustomException(
                "Hanya admin yang dapat melakukan hard delete",
                HttpStatus.FORBIDDEN.value(),
            )
        }
        val user = masterUserRepository.findById(id).orElseThrow(){
            CustomException(
                "User dengan id $id tidak ditemukan",
                HttpStatus.NOT_FOUND.value(),
            )
        }
        masterUserRepository.deleteById(user.id)
    }


}