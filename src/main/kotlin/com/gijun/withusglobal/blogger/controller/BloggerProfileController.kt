package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.BloggerDTO
import com.gijun.withusglobal.blogger.service.BloggerProfileService
import com.gijun.withusglobal.common.dto.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogger/profile")
class BloggerProfileController(
    private val bloggerProfileService: BloggerProfileService
) {
    
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
