package com.gijun.withusglobal.admin.controller

import com.gijun.withusglobal.admin.dto.AdminReviewDTO
import com.gijun.withusglobal.admin.service.AdminReviewService
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
@RequestMapping("/api/admin/reviews")
@Tag(name = "관리자 리뷰", description = "관리자를 위한 리뷰 관리")
class AdminReviewController(
    private val adminReviewService: AdminReviewService
) {
    
    @Operation(
        summary = "모든 리뷰 조회",
        description = "시스템의 모든 리뷰 페이지네이션 목록을 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰 목록 조회 실패"
            )
        ]
    )
    @GetMapping
    fun getAllReviews(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "리뷰 상태로 필터링 (선택사항)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<AdminReviewDTO.Response>>> {
        try {
            val reviews = adminReviewService.getAllReviews(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(reviews))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "리뷰 목록 조회 실패"))
        }
    }
    
    @Operation(
        summary = "리뷰 상세 정보 조회",
        description = "특정 리뷰의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰를 찾을 수 없음"
            )
        ]
    )
    @GetMapping("/{reviewId}")
    fun getReview(
        @Parameter(description = "조회할 리뷰 ID")
        @PathVariable reviewId: Long
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.getReview(reviewId)
            return ResponseEntity.ok(CommonResponse.success(review))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "리뷰 조회 실패"))
        }
    }
    
    @Operation(
        summary = "리뷰 승인",
        description = "블로거가 제출한 리뷰를 승인합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 승인 성공",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰를 찾을 수 없거나 제출됨/수정됨 상태가 아님"
            )
        ]
    )
    @PostMapping("/{reviewId}/approve")
    fun approveReview(
        @Parameter(description = "승인할 리뷰 ID")
        @PathVariable reviewId: Long,
        @RequestBody request: AdminReviewDTO.ApproveRequest
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.approveReview(reviewId, request.feedback)
            return ResponseEntity.ok(CommonResponse.success(review, "리뷰 승인 성공"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "리뷰 승인 실패"))
        }
    }
    
    @Operation(
        summary = "리뷰 거절",
        description = "리뷰를 거절하고 블로거에게 수정을 요청합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 거절 성공",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰를 찾을 수 없거나 제출됨/수정됨 상태가 아님"
            )
        ]
    )
    @PostMapping("/{reviewId}/reject")
    fun rejectReview(
        @Parameter(description = "거절할 리뷰 ID")
        @PathVariable reviewId: Long,
        @RequestBody request: AdminReviewDTO.RejectRequest
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.rejectReview(reviewId, request.reason)
            return ResponseEntity.ok(CommonResponse.success(review, "리뷰 거절 성공"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "리뷰 거절 실패"))
        }
    }
}
