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
@Tag(name = "관리자 캠페인", description = "관리자를 위한 캠페인 관리")
class AdminCampaignController(
    private val adminCampaignService: AdminCampaignService
) {
    
    @Operation(
        summary = "모든 캠페인 조회",
        description = "시스템의 모든 캠페인 페이지네이션 목록을 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인 목록 조회 실패"
            )
        ]
    )
    @GetMapping
    fun getAllCampaigns(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "캠페인 상태로 필터링 (선택사항)") 
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
        summary = "캠페인 상세 정보 조회",
        description = "통계가 포함된 특정 캠페인의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = AdminCampaignDTO.DetailedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없음"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "조회할 캠페인 ID")
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
        summary = "캠페인 승인",
        description = "검토중(PENDING) 상태인 캠페인을 승인하여 활성(ACTIVE) 상태로 변경합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 승인 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없거나 검토중 상태가 아님"
            )
        ]
    )
    @PostMapping("/{campaignId}/approve")
    fun approveCampaign(
        @Parameter(description = "승인할 캠페인 ID")
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
