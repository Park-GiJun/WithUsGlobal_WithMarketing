package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.dto.CampaignDTO
import com.gijun.withusglobal.store.service.StoreCampaignService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/campaigns")
class StoreCampaignController(
    private val storeCampaignService: StoreCampaignService
) {
    
    @PostMapping
    fun createCampaign(@RequestBody request: CampaignDTO.Request): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.createCampaign(request)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign created successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to create campaign"))
        }
    }
    
    @PutMapping("/{campaignId}")
    fun updateCampaign(
        @PathVariable campaignId: Long,
        @RequestBody request: CampaignDTO.Request
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.updateCampaign(campaignId, request)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign updated successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to update campaign"))
        }
    }
    
    @PostMapping("/{campaignId}/submit")
    fun submitCampaignForReview(@PathVariable campaignId: Long): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.submitCampaignForReview(campaignId)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign submitted for review"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to submit campaign for review"))
        }
    }
    
    @PostMapping("/{campaignId}/cancel")
    fun cancelCampaign(@PathVariable campaignId: Long): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.cancelCampaign(campaignId)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign cancelled successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to cancel campaign"))
        }
    }
    
    @GetMapping
    fun getMyCampaigns(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<CampaignDTO.Response>>> {
        try {
            val campaigns = storeCampaignService.getMyCampaigns(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(campaigns))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaigns"))
        }
    }
    
    @GetMapping("/{campaignId}")
    fun getCampaign(@PathVariable campaignId: Long): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.getCampaign(campaignId)
            return ResponseEntity.ok(CommonResponse.success(campaign))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaign"))
        }
    }
}
