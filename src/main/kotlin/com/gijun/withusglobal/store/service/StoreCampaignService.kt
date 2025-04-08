package com.gijun.withusglobal.store.service

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
import java.time.LocalDate

@Service
class StoreCampaignService(
    private val storeProfileService: StoreProfileService,
    private val campaignRepository: CampaignRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    @Transactional
    fun createCampaign(request: CampaignDTO.Request): Campaign {
        val store = storeProfileService.getCurrentStore()
        
        // Validate date constraints
        validateCampaignDates(
            request.applicationStartDate,
            request.applicationEndDate,
            request.experienceStartDate,
            request.experienceEndDate,
            request.reviewDueDate
        )
        
        val campaign = Campaign(
            store = store,
            title = request.title,
            description = request.description,
            category = request.category,
            totalSlots = request.totalSlots,
            remainingSlots = request.totalSlots,
            benefitDescription = request.benefitDescription,
            requiredPostType = request.requiredPostType,
            applicationStartDate = request.applicationStartDate,
            applicationEndDate = request.applicationEndDate,
            experienceStartDate = request.experienceStartDate,
            experienceEndDate = request.experienceEndDate,
            reviewDueDate = request.reviewDueDate,
            imageUrls = request.imageUrls.toMutableList(),
            status = Campaign.CampaignStatus.DRAFT
        )
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.CREATED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_CREATED, campaignMessage)
        
        // Notify admin
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.CAMPAIGN_STATUS_CHANGED,
            recipientId = 0, // Admin ID (will be picked up by all admin users)
            recipientRole = "ADMIN",
            title = "New Campaign Created",
            message = "A new campaign has been created: ${savedCampaign.title}",
            resourceId = savedCampaign.id,
            resourceType = "CAMPAIGN"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return savedCampaign
    }
    
    @Transactional
    fun updateCampaign(campaignId: Long, request: CampaignDTO.Request): Campaign {
        val store = storeProfileService.getCurrentStore()
        
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign belongs to this store
        if (campaign.store.id != store.id) {
            throw IllegalStateException("Campaign does not belong to current store")
        }
        
        // Check if campaign can be updated (only DRAFT or PENDING status)
        if (campaign.status != Campaign.CampaignStatus.DRAFT && campaign.status != Campaign.CampaignStatus.PENDING) {
            throw IllegalStateException("Campaign cannot be updated in current status: ${campaign.status}")
        }
        
        // Validate date constraints
        validateCampaignDates(
            request.applicationStartDate,
            request.applicationEndDate,
            request.experienceStartDate,
            request.experienceEndDate,
            request.reviewDueDate
        )
        
        campaign.update(
            title = request.title,
            description = request.description,
            category = request.category,
            totalSlots = request.totalSlots,
            benefitDescription = request.benefitDescription,
            requiredPostType = request.requiredPostType,
            applicationStartDate = request.applicationStartDate,
            applicationEndDate = request.applicationEndDate,
            experienceStartDate = request.experienceStartDate,
            experienceEndDate = request.experienceEndDate,
            reviewDueDate = request.reviewDueDate,
            imageUrls = request.imageUrls
        )
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.UPDATED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_UPDATED, campaignMessage)
        
        return savedCampaign
    }
    
    @Transactional
    fun submitCampaignForReview(campaignId: Long): Campaign {
        val store = storeProfileService.getCurrentStore()
        
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign belongs to this store
        if (campaign.store.id != store.id) {
            throw IllegalStateException("Campaign does not belong to current store")
        }
        
        // Check if campaign is in DRAFT status
        if (campaign.status != Campaign.CampaignStatus.DRAFT) {
            throw IllegalStateException("Only draft campaigns can be submitted for review")
        }
        
        // Update status to PENDING
        campaign.status = Campaign.CampaignStatus.PENDING
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_STATUS_CHANGED, campaignMessage)
        
        // Notify admin
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.CAMPAIGN_STATUS_CHANGED,
            recipientId = 0, // Admin ID (will be picked up by all admin users)
            recipientRole = "ADMIN",
            title = "Campaign Pending Review",
            message = "A campaign needs your review: ${savedCampaign.title}",
            resourceId = savedCampaign.id,
            resourceType = "CAMPAIGN"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return savedCampaign
    }
    
    @Transactional
    fun cancelCampaign(campaignId: Long): Campaign {
        val store = storeProfileService.getCurrentStore()
        
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign belongs to this store
        if (campaign.store.id != store.id) {
            throw IllegalStateException("Campaign does not belong to current store")
        }
        
        // Check if campaign can be cancelled
        if (campaign.status == Campaign.CampaignStatus.COMPLETED || 
            campaign.status == Campaign.CampaignStatus.CANCELLED) {
            throw IllegalStateException("Campaign cannot be cancelled in current status: ${campaign.status}")
        }
        
        // Update status to CANCELLED
        campaign.status = Campaign.CampaignStatus.CANCELLED
        
        val savedCampaign = campaignRepository.save(campaign)
        
        // Send Kafka message
        val campaignMessage = CampaignMessage.fromCampaign(
            savedCampaign, 
            CampaignMessage.CampaignAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendCampaignMessage(KafkaTopics.CAMPAIGN_STATUS_CHANGED, campaignMessage)
        
        return savedCampaign
    }
    
    fun getMyCampaigns(
        page: Int, 
        size: Int,
        status: String? = null
    ): PagedResponse<CampaignDTO.Response> {
        val store = storeProfileService.getCurrentStore()
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        
        val campaigns = if (status != null) {
            val campaignStatus = Campaign.CampaignStatus.valueOf(status.uppercase())
            campaignRepository.findByStoreAndStatus(store, campaignStatus, pageable)
        } else {
            campaignRepository.findByStore(store, pageable)
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
    
    fun getCampaign(campaignId: Long): CampaignDTO.Response {
        val store = storeProfileService.getCurrentStore()
        
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign belongs to this store
        if (campaign.store.id != store.id) {
            throw IllegalStateException("Campaign does not belong to current store")
        }
        
        return CampaignDTO.Response.fromEntity(campaign)
    }
    
    private fun validateCampaignDates(
        applicationStartDate: LocalDate,
        applicationEndDate: LocalDate,
        experienceStartDate: LocalDate,
        experienceEndDate: LocalDate,
        reviewDueDate: LocalDate
    ) {
        val today = LocalDate.now()
        
        // Application start date should be today or in the future
        if (applicationStartDate.isBefore(today)) {
            throw IllegalArgumentException("Application start date must be today or in the future")
        }
        
        // Application end date should be after application start date
        if (applicationEndDate.isBefore(applicationStartDate)) {
            throw IllegalArgumentException("Application end date must be after application start date")
        }
        
        // Experience start date should be after application end date
        if (experienceStartDate.isBefore(applicationEndDate)) {
            throw IllegalArgumentException("Experience start date must be after application end date")
        }
        
        // Experience end date should be after experience start date
        if (experienceEndDate.isBefore(experienceStartDate)) {
            throw IllegalArgumentException("Experience end date must be after experience start date")
        }
        
        // Review due date should be after experience end date
        if (reviewDueDate.isBefore(experienceEndDate)) {
            throw IllegalArgumentException("Review due date must be after experience end date")
        }
    }
}
