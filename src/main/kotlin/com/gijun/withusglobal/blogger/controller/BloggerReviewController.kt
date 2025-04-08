package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.ReviewDTO
import com.gijun.withusglobal.blogger.service.BloggerReviewService
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
@RequestMapping("/api/blogger/reviews")
@Tag(name = "블로거 리뷰", description = "블로거를 위한 캠페인 리뷰 관리")
class BloggerReviewController(
    private val bloggerReviewService: BloggerReviewService
) {
    
    @Operation(
        summary = "리뷰 작성",
        description = "스토어 방문 후 승인된 캠페인 지원에 대한 리뷰를 작성합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 작성 성공",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 지원서가 적절한 상태가 아님"
            )
        ]
    )
    @PostMapping
    fun submitReview(@RequestBody request: ReviewDTO.Request): ResponseEntity<CommonResponse<ReviewDTO.Response>> {
        try {
            val review = bloggerReviewService.submitReview(request)
            val response = ReviewDTO.Response.fromEntity(review)
            return ResponseEntity.ok(CommonResponse.success(response, "Review submitted successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to submit review"))
        }
    }
    
    @Operation(
        summary = "모든 리뷰 조회",
        description = "현재 블로거가 작성한 모든 리뷰의 페이지네이션 목록을 조회합니다",
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
    fun getMyReviews(
        @Parameter(description = "페이지 번호 (0부터 시작)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기") 
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<CommonResponse<PagedResponse<ReviewDTO.Response>>> {
        try {
            val reviews = bloggerReviewService.getMyReviews(page, size)
            return ResponseEntity.ok(CommonResponse.success(reviews))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get reviews"))
        }
    }
    
    @Operation(
        summary = "리뷰 상세 정보 조회",
        description = "특정 리뷰의 상세 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰를 찾을 수 없거나 현재 사용자의 소유가 아님"
            )
        ]
    )
    @GetMapping("/{reviewId}")
    fun getReview(
        @Parameter(description = "조회할 리뷰 ID")
        @PathVariable reviewId: Long
    ): ResponseEntity<CommonResponse<ReviewDTO.Response>> {
        try {
            val review = bloggerReviewService.getReview(reviewId)
            return ResponseEntity.ok(CommonResponse.success(review))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get review"))
        }
    }
    
    @Operation(
        summary = "리뷰 수정",
        description = "기존 리뷰를 수정합니다. 특히 거절된 리뷰를 수정해야 할 때 유용합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "리뷰 수정 성공",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "리뷰를 찾을 수 없거나, 현재 사용자의 소유가 아니거나, 수정 가능한 상태가 아님"
            )
        ]
    )
    @PutMapping("/{reviewId}")
    fun updateReview(
        @Parameter(description = "수정할 리뷰 ID")
        @PathVariable reviewId: Long,
        @RequestBody request: ReviewDTO.Request
    ): ResponseEntity<CommonResponse<ReviewDTO.Response>> {
        try {
            val review = bloggerReviewService.updateReview(reviewId, request)
            val response = ReviewDTO.Response.fromEntity(review)
            return ResponseEntity.ok(CommonResponse.success(response, "Review updated successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to update review"))
        }
    }
}
