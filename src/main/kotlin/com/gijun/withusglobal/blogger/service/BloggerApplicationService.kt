package com.gijun.withusglobal.blogger.service

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.dto.ApplicationDTO
import com.gijun.withusglobal.blogger.repository.ApplicationRepository
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.common.kafka.KafkaTopics
import com.gijun.withusglobal.common.kafka.message.ApplicationMessage
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.producer.KafkaProducer
import com.gijun.withusglobal.store.domain.Campaign
import com.gijun.withusglobal.store.repository.CampaignRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class BloggerApplicationService(
    private val bloggerProfileService: BloggerProfileService,
    private val applicationRepository: ApplicationRepository,
    private val campaignRepository: CampaignRepository,
    private val kafkaProducer: KafkaProducer
) {
    
    @Transactional
    fun applyToCampaign(request: ApplicationDTO.Request): Application {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val campaign = campaignRepository.findById(request.campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Validate campaign is active and accepting applications
        if (campaign.status != Campaign.CampaignStatus.ACTIVE) {
            throw IllegalStateException("Campaign is not currently accepting applications")
        }
        
        // Check application end date
        if (campaign.applicationEndDate.isBefore(LocalDate.now())) {
            throw IllegalStateException("Application period has ended")
        }
        
        // Check if already applied
        if (applicationRepository.findByCampaignAndBlogger(campaign, blogger).isPresent) {
            throw IllegalStateException("You have already applied to this campaign")
        }
        
        // Check if slots available
        if (campaign.remainingSlots <= 0) {
            throw IllegalStateException("No slots available for this campaign")
        }
        
        val application = Application(
            campaign = campaign,
            blogger = blogger,
            motivation = request.motivation,
            additionalInfo = request.additionalInfo
        )
        
        val savedApplication = applicationRepository.save(application)
        
        // Send Kafka message
        val applicationMessage = ApplicationMessage.fromApplication(
            savedApplication, 
            ApplicationMessage.ApplicationAction.CREATED
        )
        
        kafkaProducer.sendApplicationMessage(KafkaTopics.APPLICATION_CREATED, applicationMessage)
        
        // Notify store owner
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.APPLICATION_SUBMITTED,
            recipientId = campaign.store.id!!,
            recipientRole = "STORE",
            title = "New Application",
            message = "A new blogger has applied to your campaign: ${campaign.title}",
            resourceId = savedApplication.id,
            resourceType = "APPLICATION"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_APPLICATION, notificationMessage)
        
        return savedApplication
    }
    
    @Transactional
    fun cancelApplication(applicationId: Long): Application {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to this blogger
        if (application.blogger.id != blogger.id) {
            throw IllegalStateException("Application does not belong to current blogger")
        }
        
        // Check if application can be cancelled
        if (application.status != Application.ApplicationStatus.PENDING && 
            application.status != Application.ApplicationStatus.APPROVED) {
            throw IllegalStateException("Application cannot be cancelled in current status: ${application.status}")
        }
        
        application.cancel()
        
        // Send Kafka message
        val applicationMessage = ApplicationMessage.fromApplication(
            application, 
            ApplicationMessage.ApplicationAction.STATUS_CHANGED
        )
        
        kafkaProducer.sendApplicationMessage(KafkaTopics.APPLICATION_STATUS_CHANGED, applicationMessage)
        
        // Notify store owner
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.APPLICATION_SUBMITTED,
            recipientId = application.campaign.store.id!!,
            recipientRole = "STORE",
            title = "Application Cancelled",
            message = "A blogger has cancelled their application to your campaign: ${application.campaign.title}",
            resourceId = application.id,
            resourceType = "APPLICATION"
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_APPLICATION, notificationMessage)
        
        return applicationRepository.save(application)
    }
    
    fun getMyApplications(page: Int, size: Int): PagedResponse<ApplicationDTO.Response> {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val applications = applicationRepository.findByBlogger(blogger, pageable)
        
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
    
    fun getApplication(applicationId: Long): ApplicationDTO.Response {
        val blogger = bloggerProfileService.getCurrentBlogger()
        
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        
        // Check if application belongs to this blogger
        if (application.blogger.id != blogger.id) {
            throw IllegalStateException("Application does not belong to current blogger")
        }
        
        return ApplicationDTO.Response.fromEntity(application)
    }
}
