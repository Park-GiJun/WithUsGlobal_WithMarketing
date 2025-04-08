package com.gijun.withusglobal.common.kafka.message

import java.time.LocalDateTime

data class NotificationMessage(
    val type: NotificationType,
    val recipientId: Long,
    val recipientRole: String,
    val title: String,
    val message: String,
    val resourceId: Long? = null,
    val resourceType: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    enum class NotificationType {
        CAMPAIGN_STATUS_CHANGED,
        APPLICATION_SUBMITTED,
        APPLICATION_APPROVED,
        APPLICATION_REJECTED,
        REVIEW_SUBMITTED,
        REVIEW_APPROVED,
        REVIEW_REJECTED,
        SYSTEM_ANNOUNCEMENT,
        DUE_DATE_REMINDER
    }
}
