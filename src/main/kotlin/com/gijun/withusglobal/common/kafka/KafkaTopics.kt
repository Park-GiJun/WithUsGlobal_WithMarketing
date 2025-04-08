package com.gijun.withusglobal.common.kafka

object KafkaTopics {
    // User related topics
    const val USER_CREATED = "user-created"
    const val USER_UPDATED = "user-updated"
    
    // Campaign related topics
    const val CAMPAIGN_CREATED = "campaign-created"
    const val CAMPAIGN_UPDATED = "campaign-updated"
    const val CAMPAIGN_STATUS_CHANGED = "campaign-status-changed"
    
    // Application related topics
    const val APPLICATION_CREATED = "application-created"
    const val APPLICATION_STATUS_CHANGED = "application-status-changed"
    
    // Review related topics
    const val REVIEW_CREATED = "review-created"
    const val REVIEW_UPDATED = "review-updated"
    const val REVIEW_STATUS_CHANGED = "review-status-changed"
    
    // Notification topics
    const val NOTIFICATION_CAMPAIGN = "notification-campaign"
    const val NOTIFICATION_APPLICATION = "notification-application"
    const val NOTIFICATION_REVIEW = "notification-review"
}
