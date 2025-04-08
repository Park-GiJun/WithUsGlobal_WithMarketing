package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.dto.CampaignDTO
import com.gijun.withusglobal.store.service.StoreCampaignService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/campaigns")
@Tag(name = "Store Campaigns", description = "Campaign Management for Store Owners")
class StoreCampaignController(
    private val storeCampaignService: StoreCampaignService
) {
    
    @Operation(
        summary = "Create a new campaign",
        description = "Create a new marketing campaign for bloggers to apply to",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign created successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or date constraints"
            )
        ]
    )
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
    
    @Operation(
        summary = "Update a campaign",
        description = "Update an existing campaign that is in DRAFT or PENDING status",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign updated successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input, campaign not found, or campaign in wrong status"
            )
        ]
    )
    @PutMapping("/{campaignId}")
    fun updateCampaign(
        @Parameter(description = "ID of the campaign to update")
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
    
    @Operation(
        summary = "Submit campaign for review",
        description = "Submit a draft campaign for admin review before it can be published",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign submitted for review",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or not in DRAFT status"
            )
        ]
    )
    @PostMapping("/{campaignId}/submit")
    fun submitCampaignForReview(
        @Parameter(description = "ID of the campaign to submit")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.submitCampaignForReview(campaignId)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign submitted for review"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to submit campaign for review"))
        }
    }
    
    @Operation(
        summary = "Cancel a campaign",
        description = "Cancel an existing campaign that has not been completed",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign cancelled successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or already completed/cancelled"
            )
        ]
    )
    @PostMapping("/{campaignId}/cancel")
    fun cancelCampaign(
        @Parameter(description = "ID of the campaign to cancel")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.cancelCampaign(campaignId)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign cancelled successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to cancel campaign"))
        }
    }
    
    @Operation(
        summary = "Get store campaigns",
        description = "Retrieve a paginated list of all campaigns created by the current store",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved campaigns",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve campaigns"
            )
        ]
    )
    @GetMapping
    fun getMyCampaigns(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Filter by campaign status (optional)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<CampaignDTO.Response>>> {
        try {
            val campaigns = storeCampaignService.getMyCampaigns(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(campaigns))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaigns"))
        }
    }
    
    @Operation(
        summary = "Get campaign details",
        description = "Get detailed information about a specific campaign",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved campaign details",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or not owned by current store"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "ID of the campaign to retrieve")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = storeCampaignService.getCampaign(campaignId)
            return ResponseEntity.ok(CommonResponse.success(campaign))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaign"))
        }
    }
}
