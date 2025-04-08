package com.gijun.withusglobal.common.kafka.message

import com.gijun.withusglobal.blogger.domain.Application

data class ApplicationMessage(
    val applicationId: Long,
    val campaignId: Long,
    val bloggerId: Long,
    val status: String,
    val action: ApplicationAction
) {
    enum class ApplicationAction {
        CREATED,
        STATUS_CHANGED
    }
    
    companion object {
        fun fromApplication(application: Application, action: ApplicationAction): ApplicationMessage {
            return ApplicationMessage(
                applicationId = application.id!!,
                campaignId = application.campaign.id!!,
                bloggerId = application.blogger.id!!,
                status = application.status.name,
                action = action
            )
        }
    }
}
