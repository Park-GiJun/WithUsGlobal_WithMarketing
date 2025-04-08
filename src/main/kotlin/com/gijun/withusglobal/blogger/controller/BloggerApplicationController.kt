package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.ApplicationDTO
import com.gijun.withusglobal.blogger.service.BloggerApplicationService
import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogger/applications")
@Tag(name = "블로거 지원", description = "블로거를 위한 캠페인 지원 관리")
class BloggerApplicationController(
    private val bloggerApplicationService: BloggerApplicationService
) {
    
    @Operation(
        summary = "캠페인 지원",
        description = "마케팅 캠페인 참여에 지원합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원서 제출 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 지원 불가한 캠페인"
            )
        ]
    )
    @PostMapping
    fun applyToCampaign(@RequestBody request: ApplicationDTO.Request): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.applyToCampaign(request)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application submitted successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to submit application"))
        }
    }
    
    @Operation(
        summary = "지원 취소",
        description = "이전에 제출한 지원을 취소합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 취소 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 취소 실패 - 잘못된 상태이거나 현재 사용자의 소유가 아님"
            )
        ]
    )
    @DeleteMapping("/{applicationId}")
    fun cancelApplication(
        @Parameter(description = "취소할 지원 ID") 
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.cancelApplication(applicationId)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application cancelled successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to cancel application"))
        }
    }
    
    @Operation(
        summary = "모든 지원 조회",
        description = "현재 블로거가 제출한 모든 지원의 페이지네이션 목록을 조회합니다",
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
    fun getMyApplications(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = bloggerApplicationService.getMyApplications(page, size)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
        }
    }
    
    @Operation(
        summary = "지원 상세 정보 조회",
        description = "특정 지원의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원을 찾을 수 없거나 현재 사용자의 소유가 아님"
            )
        ]
    )
    @GetMapping("/{applicationId}")
    fun getApplication(
        @Parameter(description = "조회할 지원 ID")
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.getApplication(applicationId)
            return ResponseEntity.ok(CommonResponse.success(application))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get application"))
        }
    }
}
