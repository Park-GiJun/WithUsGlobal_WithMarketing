package com.gijun.withusglobal.store.dto

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.dto.BloggerDTO
import java.time.LocalDateTime

class ApplicationDTO {
    data class Response(
        val id: Long?,
        val campaignId: Long,
        val campaignTitle: String,
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
                    campaignId = application.campaign.id!!,
                    campaignTitle = application.campaign.title,
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
    
    data class DetailedResponse(
        val id: Long?,
        val campaignId: Long,
        val campaignTitle: String,
        val blogger: BloggerDTO.Response,
        val motivation: String,
        val additionalInfo: String?,
        val status: String,
        val rejectionReason: String?,
        val approvedAt: LocalDateTime?,
        val rejectedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(application: Application): DetailedResponse {
                return DetailedResponse(
                    id = application.id,
                    campaignId = application.campaign.id!!,
                    campaignTitle = application.campaign.title,
                    blogger = BloggerDTO.Response.fromEntity(application.blogger),
                    motivation = application.motivation,
                    additionalInfo = application.additionalInfo,
                    status = application.status.name,
                    rejectionReason = application.rejectionReason,
                    approvedAt = application.approvedAt,
                    rejectedAt = application.rejectedAt,
                    createdAt = application.createdAt,
                    updatedAt = application.updatedAt
                )
            }
        }
    }
    
    data class RejectRequest(
        val reason: String
    )
}
