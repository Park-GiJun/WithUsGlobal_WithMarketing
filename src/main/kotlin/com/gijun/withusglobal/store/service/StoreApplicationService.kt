package com.gijun.withusglobal.store.service

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.repository.ApplicationRepository
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.common.kafka.KafkaTopics
import com.gijun.withusglobal.common.kafka.message.ApplicationMessage
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.producer.KafkaProducer
import com.gijun.withusglobal.store.repository.CampaignRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreApplicationService(
    private val storeProfileService: StoreProfileService,
    private val campaignRepository: CampaignRepository,
    private val applicationRepository: ApplicationRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    fun getApplicationsForCampaign(
        campaignId: Long,
        page: Int,
        size: Int,
        status: String? = null
    ): PagedResponse<ApplicationDTO.Response> {
        val store = storeProfileService.getCurrentStore()
        
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign belongs to this store
        if (campaign.store.id != store.id) {
            throw IllegalStateException("Campaign does not belong to current store")
        }
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        
        val applications = if (status != null) {
            val applicationStatus = Application.ApplicationStatus.valueOf(status.uppercase())
            applicationRepository.findByCampaignAndStatus(campaign, applicationStatus, pageable)
        } else {
            applicationRepository.findByCampaign(campaign, pageable)
        }
        
        val content = applications.content.map { ApplicationDTO.Response.fromEntity(it) }
        
        return PagedResponse(
            content = content,
            page = applications.number,
            size = applications.size,
            totalElements = applications.totalElements,
            totalPages = applications.totalPages,
            last = applications.isLast
        )
    }
    
    fun getStoreApplications(
        page: Int,
        size: Int,
        status: String? = null
    ): PagedResponse<ApplicationDTO.Response> {
        val store = storeProfileService.getCurrentStore()
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        
        val applications = if (status != null) {
            val applicationStatus = Application.ApplicationStatus.valueOf(status.uppercase())
            applicationRepository.findByStoreIdAndStatus(store.id!!, applicationStatus, pageable)
        } else {
            applicationRepository.findByStoreId(store.id!!, pageable)
        }
        
        val content = applications.content.map { ApplicationDTO.Response.fromEntity(it) }
        
        return PagedResponse(
            content = content,
            page = applications.number,
            size = applications.size,
            totalElements = applications.totalElements,
            totalPages = applications.totalPages,
            last = applications.isLast
        )
    }
    
    fun getApplication(applicationId: Long): ApplicationDTO.DetailedResponse {
        val store = storeProfileService.getCurrentStore()
        
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to a campaign of this store
        if (application.campaign.store.id != store.id) {
            throw IllegalStateException("Application does not belong to current store")
        }
        
        return ApplicationDTO.DetailedResponse.fromEntity(application)
    }
    
    @Transactional
    fun approveApplication(applicationId: Long): Application {
        val store = storeProfileService.getCurrentStore()
        
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to a campaign of this store
        if (application.campaign.store.id != store.id) {
            throw IllegalStateException("Application does not belong to current store")
        }
        
        // Check if application is in PENDING status
        if (application.status != Application.ApplicationStatus.PENDING) {
            throw IllegalStateException("Application is not in pending status")
        }
        
        application.approve()
        
        val savedApplication = applicationRepository.save(application)
        
        // Send Kafka message
        val applicationMessage = ApplicationMessage.fromApplication(
            savedApplication, 
            ApplicationMessage.ApplicationAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendApplicationMessage(KafkaTopics.APPLICATION_STATUS_CHANGED, applicationMessage)
        
        // Notify blogger
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.APPLICATION_APPROVED,
            recipientId = application.blogger.user.id!!,
            recipientRole = "BLOGGER",
            title = "Application Approved",
            message = "Your application for ${application.campaign.title} has been approved!",
            resourceId = savedApplication.id,
            resourceType = "APPLICATION"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_APPLICATION, notificationMessage)
        
        return savedApplication
    }
    
    @Transactional
    fun rejectApplication(applicationId: Long, reason: String): Application {
        val store = storeProfileService.getCurrentStore()
        
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to a campaign of this store
        if (application.campaign.store.id != store.id) {
            throw IllegalStateException("Application does not belong to current store")
        }
        
        // Check if application is in PENDING status
        if (application.status != Application.ApplicationStatus.PENDING) {
            throw IllegalStateException("Application is not in pending status")
        }
        
        application.reject(reason)
        
        val savedApplication = applicationRepository.save(application)
        
        // Send Kafka message
        val applicationMessage = ApplicationMessage.fromApplication(
            savedApplication, 
            ApplicationMessage.ApplicationAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendApplicationMessage(KafkaTopics.APPLICATION_STATUS_CHANGED, applicationMessage)
        
        // Notify blogger
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.APPLICATION_REJECTED,
            recipientId = application.blogger.user.id!!,
            recipientRole = "BLOGGER",
            title = "Application Rejected",
            message = "Your application for ${application.campaign.title} has been rejected. Reason: $reason",
            resourceId = savedApplication.id,
            resourceType = "APPLICATION"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_APPLICATION, notificationMessage)
        
        return savedApplication
    }
}
