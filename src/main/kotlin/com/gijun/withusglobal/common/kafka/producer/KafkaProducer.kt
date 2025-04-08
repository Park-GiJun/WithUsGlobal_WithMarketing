package com.gijun.withusglobal.common.kafka.producer

import com.gijun.withusglobal.common.kafka.message.ApplicationMessage
import com.gijun.withusglobal.common.kafka.message.CampaignMessage
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.message.ReviewMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun sendCampaignMessage(topic: String, message: CampaignMessage) {
        kafkaTemplate.send(topic, message.campaignId.toString(), message)
    }
    
    fun sendApplicationMessage(topic: String, message: ApplicationMessage) {
        kafkaTemplate.send(topic, message.applicationId.toString(), message)
    }
    
    fun sendReviewMessage(topic: String, message: ReviewMessage) {
        kafkaTemplate.send(topic, message.reviewId.toString(), message)
    }
    
    fun sendNotificationMessage(topic: String, message: NotificationMessage) {
        kafkaTemplate.send(topic, message.recipientId.toString(), message)
    }
    
    fun sendMessage(topic: String, key: String, message: Any) {
        kafkaTemplate.send(topic, key, message)
    }
}
