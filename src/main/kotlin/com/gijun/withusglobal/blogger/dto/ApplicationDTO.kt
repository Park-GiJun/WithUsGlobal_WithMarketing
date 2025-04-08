package com.gijun.withusglobal.blogger.dto

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.store.dto.CampaignDTO
import java.time.LocalDateTime

class ApplicationDTO {
    data class Request(
        val campaignId: Long,
        val motivation: String,
        val additionalInfo: String?
    )
    
    data class Response(
        val id: Long?,
        val campaign: CampaignDTO.SimpleResponse,
        val bloggerId: Long,
        val bloggerName: String,
        val motivation: String,
        val additionalInfo: String?,
        val status: String,
        val rejectionReason: String?,
        val approvedAt: LocalDateTime?,
        val rejectedAt: LocalDateTime?,
        val createdAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(application: Application): Response {
                return Response(
                    id = application.id,
                    campaign = CampaignDTO.SimpleResponse.fromEntity(application.campaign),
                    bloggerId = application.blogger.id!!,
                    bloggerName = application.blogger.user.getName(),
                    motivation = application.motivation,
                    additionalInfo = application.additionalInfo,
                    status = application.status.name,
                    rejectionReason = application.rejectionReason,
                    approvedAt = application.approvedAt,
                    rejectedAt = application.rejectedAt,
                    createdAt = application.createdAt
                )
            }
        }
    }
    
    data class SimpleResponse(
        val id: Long?,
        val campaignId: Long,
        val campaignTitle: String,
        val status: String,
        val createdAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(application: Application): SimpleResponse {
                return SimpleResponse(
                    id = application.id,
                    campaignId = application.campaign.id!!,
                    campaignTitle = application.campaign.title,
                    status = application.status.name,
                    createdAt = application.createdAt
                )
            }
        }
    }
}
