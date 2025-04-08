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
@Tag(name = "Admin Reviews", description = "Review Management for Administrators")
class AdminReviewController(
    private val adminReviewService: AdminReviewService
) {
    
    @Operation(
        summary = "Get all reviews",
        description = "Retrieve a paginated list of all reviews in the system",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved reviews",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve reviews"
            )
        ]
    )
    @GetMapping
    fun getAllReviews(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Filter by review status (optional)") 
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<AdminReviewDTO.Response>>> {
        try {
            val reviews = adminReviewService.getAllReviews(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(reviews))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get reviews"))
        }
    }
    
    @Operation(
        summary = "Get review details",
        description = "Get detailed information about a specific review",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved review details",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Review not found"
            )
        ]
    )
    @GetMapping("/{reviewId}")
    fun getReview(
        @Parameter(description = "ID of the review to retrieve")
        @PathVariable reviewId: Long
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.getReview(reviewId)
            return ResponseEntity.ok(CommonResponse.success(review))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get review"))
        }
    }
    
    @Operation(
        summary = "Approve a review",
        description = "Approve a review submitted by a blogger",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Review approved successfully",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Review not found or not in SUBMITTED/REVISED status"
            )
        ]
    )
    @PostMapping("/{reviewId}/approve")
    fun approveReview(
        @Parameter(description = "ID of the review to approve")
        @PathVariable reviewId: Long,
        @RequestBody request: AdminReviewDTO.ApproveRequest
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.approveReview(reviewId, request.feedback)
            return ResponseEntity.ok(CommonResponse.success(review, "Review approved successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to approve review"))
        }
    }
    
    @Operation(
        summary = "Reject a review",
        description = "Reject a review and request revisions from the blogger",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Review rejected successfully",
                content = [Content(schema = Schema(implementation = AdminReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Review not found or not in SUBMITTED/REVISED status"
            )
        ]
    )
    @PostMapping("/{reviewId}/reject")
    fun rejectReview(
        @Parameter(description = "ID of the review to reject")
        @PathVariable reviewId: Long,
        @RequestBody request: AdminReviewDTO.RejectRequest
    ): ResponseEntity<CommonResponse<AdminReviewDTO.Response>> {
        try {
            val review = adminReviewService.rejectReview(reviewId, request.reason)
            return ResponseEntity.ok(CommonResponse.success(review, "Review rejected successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to reject review"))
        }
    }
}
