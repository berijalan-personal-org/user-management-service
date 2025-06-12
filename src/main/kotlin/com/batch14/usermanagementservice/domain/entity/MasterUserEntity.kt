package com.batch14.usermanagementservice.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name="mst_users")
data class MasterUserEntity(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "mst_users_id_seq"
    ) //ada 5 tipe generation type
    //identity = increment JPA
    //sequence = buat di postgree sequencenya
    @SequenceGenerator(
        name = "mst_users_id_seq",
        sequenceName = "mst_users_id_seq",
        allocationSize = 1
    )
    @Column(name="id", insertable = false, updatable = false) //disesuaikan dengan yang ada pada DB
    var id: Int = 0,

    @Column(name="email")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "username")
    var username: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id") //kolom yang mengaitkan di user
    var role: MasterRoleEntity? = null,

    @CreationTimestamp
    @Column(name="created_at", insertable = false, updatable = false) //karena current jadi false saja
    var createdAt: Timestamp? = null,

    @Column(name="created_by")
    var createdBy: String? = null,

    @UpdateTimestamp
    @Column(name="updated_at", insertable = false, updatable = false) //karena current jadi false saja
    var updatedAt: Timestamp? = null,

    @Column(name="updated_by")
    var updatedBy: String? = null,

    @Column(name="deleted_at", insertable = false, updatable = false) //karena current jadi false saja
    var deletedAt: Timestamp? = null,

    @Column(name="deleted_by")
    var deletedBy: String? = null,

    @Column(name="is_active")
    var isActive: Boolean = true,

    @Column(name="is_delete")
    var isDelete: Boolean = false
)
