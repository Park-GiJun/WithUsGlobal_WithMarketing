package com.gijun.withusglobal.store.domain

import com.gijun.withusglobal.common.domain.BaseEntity
import com.gijun.withusglobal.common.domain.User
import jakarta.persistence.*

@Entity
@Table(name = "stores")
class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,
    
    @Column(nullable = false)
    var businessName: String,
    
    @Column(nullable = false)
    var businessRegistrationNumber: String,
    
    @Column(nullable = false)
    var address: String,
    
    @Column
    var detailAddress: String? = null,
    
    @Column(nullable = false)
    var category: String,
    
    @Column
    var description: String? = null,
    
    @Column
    var phoneNumber: String? = null,
    
    @Column
    var websiteUrl: String? = null
) : BaseEntity() {
    fun update(
        businessName: String,
        address: String,
        detailAddress: String?,
        category: String,
        description: String?,
        phoneNumber: String?,
        websiteUrl: String?
    ) {
        this.businessName = businessName
        this.address = address
        this.detailAddress = detailAddress
        this.category = category
        this.description = description
        this.phoneNumber = phoneNumber
        this.websiteUrl = websiteUrl
    }
}
