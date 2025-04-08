package com.gijun.withusglobal.common.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.common.service.PublicCampaignService
import com.gijun.withusglobal.store.dto.CampaignDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/campaigns")
@Tag(name = "Public Campaigns", description = "Publicly available campaign endpoints")
class PublicCampaignController(
    private val publicCampaignService: PublicCampaignService
) {
    
    @Operation(
        summary = "Get active campaigns",
        description = "Retrieve a paginated list of all active campaigns",
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
    fun getActiveCampaigns(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Filter by category (optional)")
        @RequestParam(required = false) category: String?
    ): ResponseEntity<CommonResponse<PagedResponse<CampaignDTO.Response>>> {
        try {
            val campaigns = publicCampaignService.getActiveCampaigns(page, size, category)
            return ResponseEntity.ok(CommonResponse.success(campaigns))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaigns"))
        }
    }
    
    @Operation(
        summary = "Get campaign details",
        description = "Get detailed information about a specific active campaign",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved campaign details",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or not active"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "ID of the campaign to retrieve")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = publicCampaignService.getCampaign(campaignId)
            return ResponseEntity.ok(CommonResponse.success(campaign))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaign"))
        }
    }
    
    @Operation(
        summary = "Get campaign categories",
        description = "Retrieve a list of all available campaign categories",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved categories"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve categories"
            )
        ]
    )
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<CommonResponse<List<String>>> {
        try {
            val categories = publicCampaignService.getAllCategories()
            return ResponseEntity.ok(CommonResponse.success(categories))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get categories"))
        }
    }
}
