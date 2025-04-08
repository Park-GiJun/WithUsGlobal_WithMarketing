package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.ReviewDTO
import com.gijun.withusglobal.blogger.service.BloggerReviewService
import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogger/reviews")
class BloggerReviewController(
    private val bloggerReviewService: BloggerReviewService
) {
    
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
    
    @GetMapping
    fun getMyReviews(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<CommonResponse<PagedResponse<ReviewDTO.Response>>> {
        try {
            val reviews = bloggerReviewService.getMyReviews(page, size)
            return ResponseEntity.ok(CommonResponse.success(reviews))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get reviews"))
        }
    }
    
    @GetMapping("/{reviewId}")
    fun getReview(@PathVariable reviewId: Long): ResponseEntity<CommonResponse<ReviewDTO.Response>> {
        try {
            val review = bloggerReviewService.getReview(reviewId)
            return ResponseEntity.ok(CommonResponse.success(review))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get review"))
        }
    }
    
    @PutMapping("/{reviewId}")
    fun updateReview(
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
