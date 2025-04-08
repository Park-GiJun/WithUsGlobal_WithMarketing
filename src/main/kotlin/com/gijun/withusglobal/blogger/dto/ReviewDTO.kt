package com.gijun.withusglobal.blogger.dto

import com.gijun.withusglobal.blogger.domain.Review
import java.time.LocalDateTime

class ReviewDTO {
    data class Request(
        val applicationId: Long,
        val postUrl: String,
        val title: String?,
        val content: String?,
        val imageUrls: List<String>
    )
    
    data class Response(
        val id: Long?,
        val applicationId: Long,
        val campaignId: Long,
        val campaignTitle: String,
        val bloggerId: Long,
        val bloggerName: String,
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
                return Response(
                    id = review.id,
                    applicationId = review.application.id!!,
                    campaignId = review.application.campaign.id!!,
                    campaignTitle = review.application.campaign.title,
                    bloggerId = review.application.blogger.id!!,
                    bloggerName = review.application.blogger.user.getName(),
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
