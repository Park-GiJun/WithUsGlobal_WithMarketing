package com.gijun.withusglobal.admin.controller

import com.gijun.withusglobal.admin.dto.AdminCampaignDTO
import com.gijun.withusglobal.admin.service.AdminCampaignService
import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
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
@RequestMapping("/api/admin/campaigns")
@Tag(name = "Admin Campaigns", description = "Campaign Management for Administrators")
class AdminCampaignController(
    private val adminCampaignService: AdminCampaignService
) {
    
    @Operation(
        summary = "Get all campaigns",
        description = "Retrieve a paginated list of all campaigns in the system",
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
    fun getAllCampaigns(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Filter by campaign status (optional)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<CampaignDTO.Response>>> {
        try {
            val campaigns = adminCampaignService.getAllCampaigns(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(campaigns))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaigns"))
        }
    }
    
    @Operation(
        summary = "Get campaign details",
        description = "Get detailed information about a specific campaign with statistics",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved campaign details",
                content = [Content(schema = Schema(implementation = AdminCampaignDTO.DetailedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "ID of the campaign to retrieve")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<AdminCampaignDTO.DetailedResponse>> {
        try {
            val campaign = adminCampaignService.getCampaignWithStats(campaignId)
            return ResponseEntity.ok(CommonResponse.success(campaign))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get campaign"))
        }
    }
    
    @Operation(
        summary = "Approve a campaign",
        description = "Approve a campaign that is in PENDING status and make it ACTIVE",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign approved successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or not in PENDING status"
            )
        ]
    )
    @PostMapping("/{campaignId}/approve")
    fun approveCampaign(
        @Parameter(description = "ID of the campaign to approve")
        @PathVariable campaignId: Long
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = adminCampaignService.approveCampaign(campaignId)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign approved successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to approve campaign"))
        }
    }
    
    @Operation(
        summary = "Reject a campaign",
        description = "Reject a campaign that is in PENDING status and return it to DRAFT status",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign rejected successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or not in PENDING status"
            )
        ]
    )
    @PostMapping("/{campaignId}/reject")
    fun rejectCampaign(
        @Parameter(description = "ID of the campaign to reject")
        @PathVariable campaignId: Long,
        @RequestBody message: Map<String, String>
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val rejectionMessage = message["reason"] ?: ""
            val campaign = adminCampaignService.rejectCampaign(campaignId, rejectionMessage)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign rejected successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to reject campaign"))
        }
    }
    
    @Operation(
        summary = "Update campaign status",
        description = "Update the status of a campaign manually",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Campaign status updated successfully",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Campaign not found or invalid status transition"
            )
        ]
    )
    @PostMapping("/{campaignId}/status")
    fun updateCampaignStatus(
        @Parameter(description = "ID of the campaign to update")
        @PathVariable campaignId: Long,
        @RequestBody request: AdminCampaignDTO.StatusUpdateRequest
    ): ResponseEntity<CommonResponse<CampaignDTO.Response>> {
        try {
            val campaign = adminCampaignService.updateCampaignStatus(campaignId, request)
            val response = CampaignDTO.Response.fromEntity(campaign)
            return ResponseEntity.ok(CommonResponse.success(response, "Campaign status updated successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to update campaign status"))
        }
    }
}
