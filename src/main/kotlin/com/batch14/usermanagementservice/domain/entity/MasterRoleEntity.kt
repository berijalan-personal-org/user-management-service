package com.batch14.usermanagementservice.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name="mst_roles")
data class MasterRoleEntity(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "mst_roles_id_seq"
    ) //ada 5 tipe generation type
    //identity = increment JPA
    //sequence = buat di postgree sequencenya
    @SequenceGenerator(
        name = "mst_roles_id_seq",
        sequenceName = "mst_roles_id_seq",
        allocationSize = 1
    )
    @Column(name="id", insertable = false, updatable = false) //disesuaikan dengan yang ada pada DB
    var id: Int,

    @Column(name="name")
    var name: String,

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
