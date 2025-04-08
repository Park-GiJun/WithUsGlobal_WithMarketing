package com.gijun.withusglobal.admin.service

import com.gijun.withusglobal.admin.dto.AdminCampaignDTO
import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.domain.Review
import com.gijun.withusglobal.blogger.repository.ApplicationRepository
import com.gijun.withusglobal.blogger.repository.ReviewRepository
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.common.kafka.KafkaTopics
import com.gijun.withusglobal.common.kafka.message.CampaignMessage
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.producer.KafkaProducer
import com.gijun.withusglobal.store.domain.Campaign
import com.gijun.withusglobal.store.dto.CampaignDTO
import com.gijun.withusglobal.store.repository.CampaignRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminCampaignService(
    private val campaignRepository: CampaignRepository,
    private val applicationRepository: ApplicationRepository,
    private val reviewRepository: ReviewRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    fun getAllCampaigns(
        page: Int, 
        size: Int,
        status: String? = null
    ): PagedResponse<CampaignDTO.Response> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        
        val campaigns = if (status != null) {
            val campaignStatus = Campaign.CampaignStatus.valueOf(status.uppercase())
            campaignRepository.findByStatus(campaignStatus, pageable)
        } else {
            campaignRepository.findAll(pageable)
        }
        
        val content = campaigns.content.map { CampaignDTO.Response.fromEntity(it) }
        
        return PagedResponse(
            content = content,
            page = campaigns.number,
            size = campaigns.size,
            totalElements = campaigns.totalElements,
            totalPages = campaigns.totalPages,
            last = campaigns.isLast
        )
    }
    
    fun getCampaignWithStats(campaignId: Long): AdminCampaignDTO.DetailedResponse {
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        val totalApplications = applicationRepository.countByCampaign(campaign)
        val approvedApplications = applicationRepository.countByCampaignAndStatus(
            campaign, Application.ApplicationStatus.APPROVED
        )
        
        // Count completed reviews (reviews where status is APPROVED)
        val completedReviews = applicationRepository.findByCampaign(campaign, PageRequest.of(0, Int.MAX_VALUE))
            .content
            .mapNotNull { application -> 
                reviewRepository.findByApplication(application).orElse(null)
            }
            .count { review -> 
                review.status == Review.ReviewStatus.APPROVED 
            }
        
        return AdminCampaignDTO.DetailedResponse.fromEntity(
            campaign,
            totalApplications.toInt(),
            approvedApplications.toInt(),
            completedReviews
        )
    }
    
    @Transactional
    fun approveCampaign(campaignId: Long): Campaign {
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        if (campaign.status != Campaign.CampaignStatus.PENDING) {
            throw IllegalStateException("Campaign must be in PENDING status to be approved")
        }
        
        campaign.publishCampaign()
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_STATUS_CHANGED, campaignMessage)
        
        // Notify store owner
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.CAMPAIGN_STATUS_CHANGED,
            recipientId = campaign.store.id!!,
            recipientRole = "STORE",
            title = "Campaign Approved",
            message = "Your campaign '${campaign.title}' has been approved and is now active",
            resourceId = savedCampaign.id,
            resourceType = "CAMPAIGN"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return savedCampaign
    }
    
    @Transactional
    fun rejectCampaign(campaignId: Long, message: String): Campaign {
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        if (campaign.status != Campaign.CampaignStatus.PENDING) {
            throw IllegalStateException("Campaign must be in PENDING status to be rejected")
        }
        
        // Set status back to DRAFT
        campaign.status = Campaign.CampaignStatus.DRAFT
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_STATUS_CHANGED, campaignMessage)
        
        // Notify store owner
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.CAMPAIGN_STATUS_CHANGED,
            recipientId = campaign.store.id!!,
            recipientRole = "STORE",
            title = "Campaign Rejected",
            message = "Your campaign '${campaign.title}' has been rejected: $message",
            resourceId = savedCampaign.id,
            resourceType = "CAMPAIGN"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return savedCampaign
    }
    
    @Transactional
    fun updateCampaignStatus(campaignId: Long, request: AdminCampaignDTO.StatusUpdateRequest): Campaign {
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        val newStatus = try {
            Campaign.CampaignStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid campaign status")
        }
        
        // Update status
        campaign.status = newStatus
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_STATUS_CHANGED, campaignMessage)
        
        // Notify store owner
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.CAMPAIGN_STATUS_CHANGED,
            recipientId = campaign.store.id!!,
            recipientRole = "STORE",
            title = "Campaign Status Updated",
            message = "Your campaign '${campaign.title}' has been updated to status: ${newStatus.name}" + 
                (if (request.message?.isNotBlank() == true) ". Message: ${request.message}" else ""),
            resourceId = savedCampaign.id,
            resourceType = "CAMPAIGN"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return savedCampaign
    }
}
