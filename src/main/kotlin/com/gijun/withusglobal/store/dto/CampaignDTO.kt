package com.gijun.withusglobal.store.dto

import com.gijun.withusglobal.store.domain.Campaign
import java.time.LocalDate
import java.time.LocalDateTime

class CampaignDTO {
    data class Request(
        val title: String,
        val description: String,
        val category: String,
        val totalSlots: Int,
        val benefitDescription: String,
        val requiredPostType: String,
        val applicationStartDate: LocalDate,
        val applicationEndDate: LocalDate,
        val experienceStartDate: LocalDate,
        val experienceEndDate: LocalDate,
        val reviewDueDate: LocalDate,
        val imageUrls: List<String>
    )
    
    data class Response(
        val id: Long?,
        val store: StoreDTO.SimpleResponse,
        val title: String,
        val description: String,
        val category: String,
        val totalSlots: Int,
        val remainingSlots: Int,
        val benefitDescription: String,
        val requiredPostType: String,
        val applicationStartDate: LocalDate,
        val applicationEndDate: LocalDate,
        val experienceStartDate: LocalDate,
        val experienceEndDate: LocalDate,
        val reviewDueDate: LocalDate,
        val imageUrls: List<String>,
        val status: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(campaign: Campaign): Response {
                return Response(
                    id = campaign.id,
                    store = StoreDTO.SimpleResponse.fromEntity(campaign.store),
                    title = campaign.title,
                    description = campaign.description,
                    category = campaign.category,
                    totalSlots = campaign.totalSlots,
                    remainingSlots = campaign.remainingSlots,
                    benefitDescription = campaign.benefitDescription,
                    requiredPostType = campaign.requiredPostType,
                    applicationStartDate = campaign.applicationStartDate,
                    applicationEndDate = campaign.applicationEndDate,
                    experienceStartDate = campaign.experienceStartDate,
                    experienceEndDate = campaign.experienceEndDate,
                    reviewDueDate = campaign.reviewDueDate,
                    imageUrls = campaign.imageUrls,
                    status = campaign.status.name,
                    createdAt = campaign.createdAt,
                    updatedAt = campaign.updatedAt
                )
            }
        }
    }
    
    data class SimpleResponse(
        val id: Long?,
        val storeId: Long,
        val storeName: String,
        val title: String,
        val category: String,
        val applicationEndDate: LocalDate,
        val status: String
    ) {
        companion object {
            fun fromEntity(campaign: Campaign): SimpleResponse {
                return SimpleResponse(
                    id = campaign.id,
                    storeId = campaign.store.id!!,
                    storeName = campaign.store.businessName,
                    title = campaign.title,
                    category = campaign.category,
                    applicationEndDate = campaign.applicationEndDate,
                    status = campaign.status.name
                )
            }
        }
    }
}
