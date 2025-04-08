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
@Tag(name = "Blogger Reviews", description = "Campaign Review Management for Bloggers")
class BloggerReviewController(
    private val bloggerReviewService: BloggerReviewService
) {
    
    @Operation(
        summary = "Submit a review",
        description = "Submit a review for an approved campaign application after visiting the store",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Review submitted successfully",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or application not in proper status"
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
        summary = "Get all reviews",
        description = "Retrieve a paginated list of all reviews submitted by the current blogger",
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
    fun getMyReviews(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
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
        summary = "Get review details",
        description = "Get detailed information about a specific review",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved review details",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Review not found or not owned by current user"
            )
        ]
    )
    @GetMapping("/{reviewId}")
    fun getReview(
        @Parameter(description = "ID of the review to retrieve")
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
        summary = "Update a review",
        description = "Update an existing review, particularly useful when a review was rejected and needs revision",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Review updated successfully",
                content = [Content(schema = Schema(implementation = ReviewDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Review not found, not owned by current user, or in an invalid status for updates"
            )
        ]
    )
    @PutMapping("/{reviewId}")
    fun updateReview(
        @Parameter(description = "ID of the review to update")
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
