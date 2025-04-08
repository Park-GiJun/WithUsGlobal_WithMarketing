package com.gijun.withusglobal.admin.service

import com.gijun.withusglobal.admin.dto.AdminReviewDTO
import com.gijun.withusglobal.blogger.domain.Review
import com.gijun.withusglobal.blogger.repository.ReviewRepository
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.common.kafka.KafkaTopics
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.message.ReviewMessage
import com.gijun.withusglobal.common.kafka.producer.KafkaProducer
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminReviewService(
    private val reviewRepository: ReviewRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    fun getAllReviews(
        page: Int, 
        size: Int,
        status: String? = null
    ): PagedResponse<AdminReviewDTO.Response> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        
        val reviews = if (status != null) {
            val reviewStatus = Review.ReviewStatus.valueOf(status.uppercase())
            reviewRepository.findByStatus(reviewStatus, pageable)
        } else {
            reviewRepository.findAll(pageable)
        }
        
        val content = reviews.content.map { AdminReviewDTO.Response.fromEntity(it) }
        
        return PagedResponse(
            content = content,
            page = reviews.number,
            size = reviews.size,
            totalElements = reviews.totalElements,
            totalPages = reviews.totalPages,
            last = reviews.isLast
        )
    }
    
    fun getReview(reviewId: Long): AdminReviewDTO.Response {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("Review not found") }
        
        return AdminReviewDTO.Response.fromEntity(review)
    }
    
    @Transactional
    fun approveReview(reviewId: Long, feedback: String?): AdminReviewDTO.Response {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("Review not found") }
        
        if (review.status != Review.ReviewStatus.SUBMITTED && review.status != Review.ReviewStatus.REVISED) {
            throw IllegalStateException("Review must be in SUBMITTED or REVISED status to be approved")
        }
        
        review.approve(feedback)
        
        val savedReview = reviewRepository.save(review)
        
        // Send Kafka message
        val reviewMessage = ReviewMessage.fromReview(
            savedReview, 
            ReviewMessage.ReviewAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendReviewMessage(KafkaTopics.REVIEW_STATUS_CHANGED, reviewMessage)
        
        // Notify blogger
        val bloggerId = review.application.blogger.user.id!!
        val storeId = review.application.campaign.store.id!!
        
        val bloggerNotification = NotificationMessage(
            type = NotificationMessage.NotificationType.REVIEW_APPROVED,
            recipientId = bloggerId,
            recipientRole = "BLOGGER",
            title = "Review Approved",
            message = "Your review for campaign '${review.application.campaign.title}' has been approved" +
                    (if (!feedback.isNullOrBlank()) ". Feedback: $feedback" else ""),
            resourceId = savedReview.id,
            resourceType = "REVIEW"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_REVIEW, bloggerNotification)
        
        // Notify store
        val storeNotification = NotificationMessage(
            type = NotificationMessage.NotificationType.REVIEW_APPROVED,
            recipientId = storeId,
            recipientRole = "STORE",
            title = "Review Approved",
            message = "A review for your campaign '${review.application.campaign.title}' has been approved",
            resourceId = savedReview.id,
            resourceType = "REVIEW"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_REVIEW, storeNotification)
        
        return AdminReviewDTO.Response.fromEntity(savedReview)
    }
    
    @Transactional
    fun rejectReview(reviewId: Long, reason: String): AdminReviewDTO.Response {
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("Review not found") }
        
        if (review.status != Review.ReviewStatus.SUBMITTED && review.status != Review.ReviewStatus.REVISED) {
            throw IllegalStateException("Review must be in SUBMITTED or REVISED status to be rejected")
        }
        
        review.reject(reason)
        
        val savedReview = reviewRepository.save(review)
        
        // Send Kafka message
        val reviewMessage = ReviewMessage.fromReview(
            savedReview, 
            ReviewMessage.ReviewAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendReviewMessage(KafkaTopics.REVIEW_STATUS_CHANGED, reviewMessage)
        
        // Notify blogger
        val bloggerId = review.application.blogger.user.id!!
        
        val notification = NotificationMessage(
            type = NotificationMessage.NotificationType.REVIEW_REJECTED,
            recipientId = bloggerId,
            recipientRole = "BLOGGER",
            title = "Review Needs Revision",
            message = "Your review for campaign '${review.application.campaign.title}' needs revision. Reason: $reason",
            resourceId = savedReview.id,
            resourceType = "REVIEW"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_REVIEW, notification)
        
        return AdminReviewDTO.Response.fromEntity(savedReview)
    }
}
