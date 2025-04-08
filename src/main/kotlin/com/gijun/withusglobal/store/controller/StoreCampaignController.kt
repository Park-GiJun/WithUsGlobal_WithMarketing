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
@Tag(name = "스토어 캠페인", description = "스토어 소유자를 위한 캠페인 관리")
class StoreCampaignController(
    private val storeCampaignService: StoreCampaignService
) {
    
    @Operation(
        summary = "새 캠페인 생성",
        description = "블로거가 지원할 수 있는 새로운 마케팅 캠페인을 생성합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 생성 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 날짜 제약 조건 오류"
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
        summary = "캠페인 수정",
        description = "초안(DRAFT) 또는 검토중(PENDING) 상태인 기존 캠페인을 수정합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 수정 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값, 캠페인 찾을 수 없음, 또는 캠페인이 잘못된 상태"
            )
        ]
    )
    @PutMapping("/{campaignId}")
    fun updateCampaign(
        @Parameter(description = "수정할 캠페인 ID")
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
        summary = "캠페인 검토 요청",
        description = "초안 상태의 캠페인을 발행하기 전에 관리자 검토를 위해 제출합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 검토 요청 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없거나 초안 상태가 아님"
            )
        ]
    )
    @PostMapping("/{campaignId}/submit")
    fun submitCampaignForReview(
        @Parameter(description = "검토 요청할 캠페인 ID")
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
        summary = "캠페인 취소",
        description = "완료되지 않은 기존 캠페인을 취소합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 취소 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없거나 이미 완료/취소됨"
            )
        ]
    )
    @PostMapping("/{campaignId}/cancel")
    fun cancelCampaign(
        @Parameter(description = "취소할 캠페인 ID")
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
        summary = "스토어 캠페인 목록 조회",
        description = "현재 스토어가 생성한 모든 캠페인의 페이지네이션 목록을 조회합니다",
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
    fun getMyCampaigns(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "캠페인 상태로 필터링 (선택사항)") 
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
        summary = "캠페인 상세 정보 조회",
        description = "특정 캠페인의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없거나 현재 스토어의 소유가 아님"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "조회할 캠페인 ID")
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
