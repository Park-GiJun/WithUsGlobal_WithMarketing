package com.gijun.withusglobal.admin.controller

import com.gijun.withusglobal.admin.dto.DashboardDTO
import com.gijun.withusglobal.admin.service.AdminDashboardService
import com.gijun.withusglobal.common.dto.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/dashboard")
@Tag(name = "관리자 대시보드", description = "관리자용 대시보드 지표")
class AdminDashboardController(
    private val adminDashboardService: AdminDashboardService
) {
    
    @Operation(
        summary = "대시보드 요약 정보 조회",
        description = "관리자 대시보드의 주요 지표 요약 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "대시보드 요약 정보 조회 성공",
                content = [Content(schema = Schema(implementation = DashboardDTO.Summary::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "대시보드 요약 정보 조회 실패"
            )
        ]
    )
    @GetMapping("/summary")
    fun getDashboardSummary(): ResponseEntity<CommonResponse<DashboardDTO.Summary>> {
        try {
            val summary = adminDashboardService.getDashboardSummary()
            return ResponseEntity.ok(CommonResponse.success(summary))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get dashboard summary"))
        }
    }
}
