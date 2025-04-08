package com.gijun.withusglobal.admin.dto

import com.gijun.withusglobal.blogger.domain.Review
import java.time.LocalDateTime

class AdminReviewDTO {
    data class ApproveRequest(
        val feedback: String?
    )
    
    data class RejectRequest(
        val reason: String
    )
    
    data class Response(
        val id: Long?,
        val applicationId: Long,
        val campaignId: Long,
        val campaignTitle: String,
        val bloggerId: Long,
        val bloggerName: String,
        val blogUrl: String,
        val storeId: Long,
        val storeName: String,
        val postUrl: String,
        val title: String?,
        val content: String?,
        val imageUrls: List<String>,
        val status: String,
        val rejectionReason: String?,
        val adminFeedback: String?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun fromEntity(review: Review): Response {
                val application = review.application
                val campaign = application.campaign
                
                return Response(
                    id = review.id,
                    applicationId = application.id!!,
                    campaignId = campaign.id!!,
                    campaignTitle = campaign.title,
                    bloggerId = application.blogger.id!!,
                    bloggerName = application.blogger.user.getName(),
                    blogUrl = application.blogger.blogUrl,
                    storeId = campaign.store.id!!,
                    storeName = campaign.store.businessName,
                    postUrl = review.postUrl,
                    title = review.title,
                    content = review.content,
                    imageUrls = review.imageUrls,
                    status = review.status.name,
                    rejectionReason = review.rejectionReason,
                    adminFeedback = review.adminFeedback,
                    createdAt = review.createdAt,
                    updatedAt = review.updatedAt
                )
            }
        }
    }
}
