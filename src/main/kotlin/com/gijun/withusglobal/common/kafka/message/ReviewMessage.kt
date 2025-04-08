package com.gijun.withusglobal.common.kafka.message

import com.gijun.withusglobal.blogger.domain.Review

data class ReviewMessage(
    val reviewId: Long,
    val applicationId: Long,
    val campaignId: Long,
    val bloggerId: Long,
    val storeId: Long,
    val status: String,
    val action: ReviewAction
) {
    enum class ReviewAction {
        CREATED,
        UPDATED,
        STATUS_CHANGED
    }
    
    companion object {
        fun fromReview(review: Review, action: ReviewAction): ReviewMessage {
            return ReviewMessage(
                reviewId = review.id!!,
                applicationId = review.application.id!!,
                campaignId = review.application.campaign.id!!,
                bloggerId = review.application.blogger.id!!,
                storeId = review.application.campaign.store.id!!,
                status = review.status.name,
                action = action
            )
        }
    }
}
