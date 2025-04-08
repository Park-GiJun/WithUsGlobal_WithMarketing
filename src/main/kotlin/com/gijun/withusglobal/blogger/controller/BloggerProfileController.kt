package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.BloggerDTO
import com.gijun.withusglobal.blogger.service.BloggerProfileService
import com.gijun.withusglobal.common.dto.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogger/profile")
@Tag(name = "Blogger Profile", description = "Blogger Profile Management")
class BloggerProfileController(
    private val bloggerProfileService: BloggerProfileService
) {
    
    @Operation(
        summary = "Get blogger profile",
        description = "Retrieve the profile information of the current blogger",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved blogger profile",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve profile or user is not a blogger"
            )
        ]
    )
    @GetMapping
    fun getProfile(): ResponseEntity<CommonResponse<BloggerDTO.Response>> {
        try {
            val blogger = bloggerProfileService.getProfile()
            val response = BloggerDTO.Response.fromEntity(blogger)
            return ResponseEntity.ok(CommonResponse.success(response))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get profile"))
        }
    }
    
    @Operation(
        summary = "Update blogger profile",
        description = "Update the profile information of the current blogger",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Profile updated successfully",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to update profile, user is not a blogger, or invalid input"
            )
        ]
    )
    @PutMapping
    fun updateProfile(@RequestBody request: BloggerDTO.Request): ResponseEntity<CommonResponse<BloggerDTO.Response>> {
        try {
            val updatedBlogger = bloggerProfileService.updateProfile(request)
            val response = BloggerDTO.Response.fromEntity(updatedBlogger)
            return ResponseEntity.ok(CommonResponse.success(response, "Profile updated successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to update profile"))
        }
    }
}
