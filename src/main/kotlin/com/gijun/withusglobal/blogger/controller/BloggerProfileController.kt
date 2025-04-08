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
@Tag(name = "블로거 프로필", description = "블로거 프로필 관리")
class BloggerProfileController(
    private val bloggerProfileService: BloggerProfileService
) {
    
    @Operation(
        summary = "블로거 프로필 조회",
        description = "현재 블로거의 프로필 정보를 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "블로거 프로필 조회 성공",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "프로필 조회 실패 또는 사용자가 블로거가 아님"
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
        summary = "블로거 프로필 수정",
        description = "현재 블로거의 프로필 정보를 수정합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "프로필 수정 성공",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "프로필 수정 실패, 사용자가 블로거가 아님, 또는 잘못된 입력값"
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
