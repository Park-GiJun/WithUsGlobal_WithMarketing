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
@Tag(name = "Admin Dashboard", description = "Dashboard metrics for Administrators")
class AdminDashboardController(
    private val adminDashboardService: AdminDashboardService
) {
    
    @Operation(
        summary = "Get dashboard summary",
        description = "Get a summary of key metrics for the admin dashboard",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved dashboard summary",
                content = [Content(schema = Schema(implementation = DashboardDTO.Summary::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve dashboard summary"
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
