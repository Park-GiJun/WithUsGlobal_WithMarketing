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
@Tag(name = "공개 캠페인", description = "공개적으로 사용 가능한 캠페인 엔드포인트")
class PublicCampaignController(
    private val publicCampaignService: PublicCampaignService
) {
    
    @Operation(
        summary = "활성 캠페인 조회",
        description = "모든 활성 캠페인의 페이지네이션 목록을 조회합니다",
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
    fun getActiveCampaigns(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "카테고리별 필터링 (선택사항)")
        @RequestParam(required = false) category: String?
    ): ResponseEntity<CommonResponse<PagedResponse<CampaignDTO.Response>>> {
        try {
            val campaigns = publicCampaignService.getActiveCampaigns(page, size, category)
            return ResponseEntity.ok(CommonResponse.success(campaigns))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "캠페인 목록 조회 실패"))
        }
    }
    
    @Operation(
        summary = "캠페인 상세 정보 조회",
        description = "특정 활성 캠페인의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "캠페인 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = CampaignDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "캠페인을 찾을 수 없거나 활성 상태가 아님"
            )
        ]
    )
    @GetMapping("/{campaignId}")
    fun getCampaign(
        @Parameter(description = "조회할 캠페인 ID")
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
        summary = "캠페인 카테고리 조회",
        description = "사용 가능한 모든 캠페인 카테고리 목록을 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "카테고리 목록 조회 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "카테고리 목록 조회 실패"
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
