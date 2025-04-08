package com.gijun.withusglobal.admin.dto

import com.gijun.withusglobal.store.domain.Campaign
import java.time.LocalDate
import java.time.LocalDateTime

class AdminCampaignDTO {
    data class StatusUpdateRequest(
        val status: String,
        val message: String? = null
    )
    
    data class DetailedResponse(
        val id: Long?,
        val storeId: Long,
        val storeName: String,
        val storeBusinessRegistrationNumber: String,
        val storeContact: String?,
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
        val updatedAt: LocalDateTime,
        val totalApplications: Int,
        val approvedApplications: Int,
        val completedReviews: Int
    ) {
        companion object {
            fun fromEntity(
                campaign: Campaign,
                totalApplications: Int,
                approvedApplications: Int,
                completedReviews: Int
            ): DetailedResponse {
                return DetailedResponse(
                    id = campaign.id,
                    storeId = campaign.store.id!!,
                    storeName = campaign.store.businessName,
                    storeBusinessRegistrationNumber = campaign.store.businessRegistrationNumber,
                    storeContact = campaign.store.phoneNumber,
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
                    updatedAt = campaign.updatedAt,
                    totalApplications = totalApplications,
                    approvedApplications = approvedApplications,
                    completedReviews = completedReviews
                )
            }
        }
    }
}
