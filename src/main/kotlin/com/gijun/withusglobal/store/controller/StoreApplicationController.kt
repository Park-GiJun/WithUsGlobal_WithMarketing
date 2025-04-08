package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.dto.ApplicationDTO
import com.gijun.withusglobal.store.service.StoreApplicationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/applications")
@Tag(name = "스토어 지원", description = "스토어 소유자를 위한 지원서 관리")
class StoreApplicationController(
    private val storeApplicationService: StoreApplicationService
) {
    
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @GetMapping("/campaign/{campaignId}")
    fun getApplicationsForCampaign(
        @Parameter(description = "캠페인 ID") 
        @PathVariable campaignId: Long,
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "지원 상태별 필터링 (선택사항)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = storeApplicationService.getApplicationsForCampaign(campaignId, page, size, status)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "지원 목록 조회 실패"))
        }
    }
    
    @Operation(
        summary = "스토어 지원 목록 조회",
        description = "현재 스토어의 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @GetMapping
    fun getStoreApplications(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "지원 상태별 필터링 (선택사항)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = storeApplicationService.getStoreApplications(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "지원 목록 조회 실패"))
        }
    }
    
    @Operation(
        summary = "지원 상세 정보 조회",
        description = "특정 지원의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.DetailedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원을 찾을 수 없거나 현재 스토어의 소유가 아님"
            )
        ]
    )
    @GetMapping("/{applicationId}")
    fun getApplication(
        @Parameter(description = "조회할 지원 ID")
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.DetailedResponse>> {
        try {
            val application = storeApplicationService.getApplication(applicationId)
            return ResponseEntity.ok(CommonResponse.success(application))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "지원 조회 실패"))
        }
    }
    
    @Operation(
        summary = "지원 승인",
        description = "지원을 승인하여 블로거에게 캠페인 참여 자격을 부여합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 승인 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원이 유효하지 않거나 이미 처리되었거나 현재 스토어의 소유가 아님"
            )
        ]
    )
    @PostMapping("/{applicationId}/approve")
    fun approveApplication(
        @Parameter(description = "승인할 지원 ID")
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = storeApplicationService.approveApplication(applicationId)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "지원 승인 성공"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "지원 승인 실패"))
        }
    }
    
    @Operation(
        summary = "지원 거절",
        description = "지원을 거절하여 블로거의 캠페인 참여를 거부합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 거절 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원이 유효하지 않거나 이미 처리되었거나 현재 스토어의 소유가 아님"
            )
        ]
    )
    @PostMapping("/{applicationId}/reject")
    fun rejectApplication(
        @Parameter(description = "거절할 지원 ID")
        @PathVariable applicationId: Long,
        @RequestBody request: ApplicationDTO.RejectRequest
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = storeApplicationService.rejectApplication(applicationId, request.reason)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "지원 거절 성공"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "지원 거절 실패"))
        }
    }
}