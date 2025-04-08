package com.gijun.withusglobal.store.dto

import com.gijun.withusglobal.store.domain.Store
import java.time.LocalDateTime

class StoreDTO {
    data class Request(
        val businessName: String,
        val businessRegistrationNumber: String,
        val address: String,
        val detailAddress: String? = null,
        val category: String,
        val description: String? = null,
        val phoneNumber: String? = null,
        val websiteUrl: String? = null
    )
    
    data class Response(
        val id: Long?,
        val userId: Long?,
        val email: String,
        val name: String,
        val businessName: String,
        val businessRegistrationNumber: String,
        val address: String,
        val detailAddress: String?,
        val category: String,
        val description: String?,
        val phoneNumber: String?,
        val websiteUrl: String?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(store: Store): Response {
                return Response(
                    id = store.id,
                    userId = store.user.id,
                    email = store.user.username,
                    name = store.user.getName(),
                    businessName = store.businessName,
                    businessRegistrationNumber = store.businessRegistrationNumber,
                    address = store.address,
                    detailAddress = store.detailAddress,
                    category = store.category,
                    description = store.description,
                    phoneNumber = store.phoneNumber,
                    websiteUrl = store.websiteUrl,
                    createdAt = store.createdAt,
                    updatedAt = store.updatedAt
                )
            }
        }
    }
    
    data class SimpleResponse(
        val id: Long?,
        val businessName: String,
        val category: String,
        val address: String
    ) {
        companion object {
            fun fromEntity(store: Store): SimpleResponse {
                return SimpleResponse(
                    id = store.id,
                    businessName = store.businessName,
                    category = store.category,
                    address = store.address
                )
            }
        }
    }
}
