package com.gijun.withusglobal.common.kafka.producer

import com.gijun.withusglobal.common.kafka.message.ApplicationMessage
import com.gijun.withusglobal.common.kafka.message.CampaignMessage
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.message.ReviewMessage
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = LoggerFactory.getLogger(KafkaProducer::class.java)
    
    fun sendCampaignMessage(topic: String, message: CampaignMessage) {
        log.info("Sending campaign message to topic: {}, campaignId: {}, action: {}", 
                 topic, message.campaignId, message.action)
        
        val future: CompletableFuture<SendResult<String, Any>> = 
            kafkaTemplate.send(topic, message.campaignId.toString(), message)
            
        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("Campaign message sent successfully, offset: {}", result.recordMetadata.offset())
            } else {
                log.error("Failed to send campaign message to topic: {}, campaignId: {}", 
                          topic, message.campaignId, ex)
            }
        }
    }
    
    fun sendApplicationMessage(topic: String, message: ApplicationMessage) {
        log.info("Sending application message to topic: {}, applicationId: {}, action: {}", 
                 topic, message.applicationId, message.action)
        
        val future: CompletableFuture<SendResult<String, Any>> = 
            kafkaTemplate.send(topic, message.applicationId.toString(), message)
            
        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("Application message sent successfully, offset: {}", result.recordMetadata.offset())
            } else {
                log.error("Failed to send application message to topic: {}, applicationId: {}", 
                          topic, message.applicationId, ex)
            }
        }
    }
    
    fun sendReviewMessage(topic: String, message: ReviewMessage) {
        log.info("Sending review message to topic: {}, reviewId: {}, action: {}", 
                 topic, message.reviewId, message.action)
        
        val future: CompletableFuture<SendResult<String, Any>> = 
            kafkaTemplate.send(topic, message.reviewId.toString(), message)
            
        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("Review message sent successfully, offset: {}", result.recordMetadata.offset())
            } else {
                log.error("Failed to send review message to topic: {}, reviewId: {}", 
                          topic, message.reviewId, ex)
            }
        }
    }
    
    fun sendNotificationMessage(topic: String, message: NotificationMessage) {
        log.info("Sending notification message to topic: {}, recipientId: {}, type: {}", 
                 topic, message.recipientId, message.type)
        
        val future: CompletableFuture<SendResult<String, Any>> = 
            kafkaTemplate.send(topic, message.recipientId.toString(), message)
            
        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("Notification message sent successfully, offset: {}", result.recordMetadata.offset())
            } else {
                log.error("Failed to send notification message to topic: {}, recipientId: {}", 
                          topic, message.recipientId, ex)
            }
        }
    }
    
    fun sendMessage(topic: String, key: String, message: Any) {
        log.info("Sending generic message to topic: {}, key: {}", topic, key)
        
        val future: CompletableFuture<SendResult<String, Any>> = 
            kafkaTemplate.send(topic, key, message)
            
        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("Generic message sent successfully, offset: {}", result.recordMetadata.offset())
            } else {
                log.error("Failed to send generic message to topic: {}, key: {}", topic, key, ex)
            }
        }
    }
}
