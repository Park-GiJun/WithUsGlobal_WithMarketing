package com.gijun.withusglobal.common.service

import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.domain.Campaign
import com.gijun.withusglobal.store.dto.CampaignDTO
import com.gijun.withusglobal.store.repository.CampaignRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PublicCampaignService(
    private val campaignRepository: CampaignRepository
) {
    
    fun getActiveCampaigns(
        page: Int, 
        size: Int,
        category: String?
    ): PagedResponse<CampaignDTO.Response> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val currentDate = LocalDate.now()
        
        val campaigns = if (category != null) {
            campaignRepository.findByCategoriesAndStatusAndAvailable(
                listOf(category),
                Campaign.CampaignStatus.ACTIVE,
                currentDate,
                pageable
            )
        } else {
            campaignRepository.findActiveAndAvailableCampaigns(
                Campaign.CampaignStatus.ACTIVE,
                currentDate,
                pageable
            )
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
        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { IllegalArgumentException("Campaign not found") }
        
        // Check if campaign is active
        if (campaign.status != Campaign.CampaignStatus.ACTIVE) {
            throw IllegalArgumentException("Campaign is not active")
        }
        
        // Check if application period is still open
        if (campaign.applicationEndDate.isBefore(LocalDate.now())) {
            throw IllegalArgumentException("Application period has ended")
        }
        
        return CampaignDTO.Response.fromEntity(campaign)
    }
    
    fun getAllCategories(): List<String> {
        return campaignRepository.findAll()
            .map { it.category }
            .distinct()
            .sorted()
    }
}
