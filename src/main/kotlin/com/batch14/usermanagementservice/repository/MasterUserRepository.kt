package com.batch14.usermanagementservice.repository

import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface MasterUserRepository: JpaRepository<MasterUserEntity, Int> {
    @Query("""
        SELECT U FROM MasterUserEntity U
        LEFT JOIN FETCH U.role
        WHERE U.isDelete = false
        AND U.isActive = true
    """, nativeQuery = false) //kalau simple pakai false saja, kalau true harus sesuai dengan DB
    fun getAllActiveUser(): List<MasterUserEntity>

    @Query("""
        SELECT U FROM MasterUserEntity U
        LEFT JOIN FETCH U.role
        WHERE U.isDelete = false
        AND U.isActive = true
        AND U.id = :id
    """, nativeQuery = false)
    fun getUserById(@Param("id") id: Int): MasterUserEntity?
    fun findOneByEmail(email: String?): MasterUserEntity?
    fun findOneByUsername(username: String?): Optional<MasterUserEntity>

}