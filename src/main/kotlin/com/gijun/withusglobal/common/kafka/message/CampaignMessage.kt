package com.gijun.withusglobal.common.kafka.message

import com.gijun.withusglobal.store.domain.Campaign

data class CampaignMessage(
    val campaignId: Long,
    val storeId: Long,
    val title: String,
    val status: String,
    val action: CampaignAction
) {
    enum class CampaignAction {
        CREATED,
        UPDATED,
        STATUS_CHANGED
    }
    
    companion object {
        fun fromCampaign(campaign: Campaign, action: CampaignAction): CampaignMessage {
            return CampaignMessage(
                campaignId = campaign.id!!,
                storeId = campaign.store.id!!,
                title = campaign.title,
                status = campaign.status.name,
                action = action
            )
        }
    }
}
