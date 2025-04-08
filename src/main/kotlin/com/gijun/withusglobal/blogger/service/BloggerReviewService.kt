package com.gijun.withusglobal.blogger.service

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.domain.Review
import com.gijun.withusglobal.blogger.dto.ReviewDTO
import com.gijun.withusglobal.blogger.repository.ApplicationRepository
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
import java.time.LocalDate

@Service
class BloggerReviewService(
    private val bloggerProfileService: BloggerProfileService,
    private val applicationRepository: ApplicationRepository,
    private val reviewRepository: ReviewRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    @Transactional
    fun submitReview(request: ReviewDTO.Request): Review {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val application = applicationRepository.findById(request.applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to this blogger
        if (application.blogger.id != blogger.id) {
            throw IllegalStateException("Application does not belong to current blogger")
        }
        
        // Check if application is approved
        if (application.status != Application.ApplicationStatus.APPROVED) {
            throw IllegalStateException("Cannot submit review for non-approved application")
        }
        
        // Check if review due date has passed
        if (application.campaign.reviewDueDate.isBefore(LocalDate.now())) {
            throw IllegalStateException("Review submission deadline has passed")
        }
        
        // Check if a review already exists for this application
        val existingReview = reviewRepository.findByApplication(application)
        
        val review = if (existingReview.isPresent) {
            // Update existing review
            val existing = existingReview.get()
            existing.update(
                postUrl = request.postUrl,
                title = request.title,
                content = request.content,
                imageUrls = request.imageUrls
            )
            existing
        } else {
            // Create new review
            Review(
                application = application,
                postUrl = request.postUrl,
                title = request.title,
                content = request.content,
                imageUrls = request.imageUrls.toMutableList()
            )
        }
        
        val savedReview = reviewRepository.save(review)
        
        // Send Kafka message
        val reviewAction = if (existingReview.isPresent) {
            ReviewMessage.ReviewAction.UPDATED
        } else {
            ReviewMessage.ReviewAction.CREATED
        }
        
        val reviewMessage = ReviewMessage.fromReview(savedReview, reviewAction)
        
        val kafkaTopic = if (existingReview.isPresent) {
            KafkaTopics.REVIEW_UPDATED
        } else {
            KafkaTopics.REVIEW_CREATED
        }
        
        kafkaProducer.sendReviewMessage(kafkaTopic, reviewMessage)
        
        // Notify store owner and admin
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.REVIEW_SUBMITTED,
            recipientId = application.campaign.store.id!!,
            recipientRole = "STORE",
            title = "New Review Submitted",
            message = "A blogger has submitted a review for your campaign: ${application.campaign.title}",
            resourceId = savedReview.id,
            resourceType = "REVIEW"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_REVIEW, notificationMessage)
        
        // Notify admin
        val adminNotificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.REVIEW_SUBMITTED,
            recipientId = 0, // Admin ID (will be picked up by all admin users)
            recipientRole = "ADMIN",
            title = "New Review Needs Approval",
            message = "A new review has been submitted for campaign: ${application.campaign.title}",
            resourceId = savedReview.id,
            resourceType = "REVIEW"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_REVIEW, adminNotificationMessage)
        
        return savedReview
    }
    
    fun getMyReviews(page: Int, size: Int): PagedResponse<ReviewDTO.Response> {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val reviews = reviewRepository.findByBloggerId(blogger.id!!, pageable)
        
        val content = reviews.content.map { ReviewDTO.Response.fromEntity(it) }
        
        return PagedResponse(
            content = content,
            page = reviews.number,
            size = reviews.size,
            totalElements = reviews.totalElements,
            totalPages = reviews.totalPages,
            last = reviews.isLast
        )
    }
    
    fun getReview(reviewId: Long): ReviewDTO.Response {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("Review not found") }
        
        // Check if review belongs to this blogger
        if (review.application.blogger.id != blogger.id) {
            throw IllegalStateException("Review does not belong to current blogger")
        }
        
        return ReviewDTO.Response.fromEntity(review)
    }
    
    @Transactional
    fun updateReview(reviewId: Long, request: ReviewDTO.Request): Review {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("Review not found") }
        
        // Check if review belongs to this blogger
        if (review.application.blogger.id != blogger.id) {
            throw IllegalStateException("Review does not belong to current blogger")
        }
        
        // Check if review can be updated
        if (review.status != Review.ReviewStatus.SUBMITTED && 
            review.status != Review.ReviewStatus.REJECTED) {
            throw IllegalStateException("Review cannot be updated in current status: ${review.status}")
        }
        
        // Update review
        review.update(
            postUrl = request.postUrl,
            title = request.title,
            content = request.content,
            imageUrls = request.imageUrls
        )
        
        val savedReview = reviewRepository.save(review)
        
        // Send Kafka message
        val reviewMessage = ReviewMessage.fromReview(savedReview, ReviewMessage.ReviewAction.UPDATED)
        kafkaProducer.sendReviewMessage(KafkaTopics.REVIEW_UPDATED, reviewMessage)
        
        return savedReview
    }
}
