package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.ApplicationDTO
import com.gijun.withusglobal.blogger.service.BloggerApplicationService
import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogger/applications")
class BloggerApplicationController(
    private val bloggerApplicationService: BloggerApplicationService
) {
    
    @PostMapping
    fun applyToCampaign(@RequestBody request: ApplicationDTO.Request): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.applyToCampaign(request)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application submitted successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to submit application"))
        }
    }
    
    @DeleteMapping("/{applicationId}")
    fun cancelApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.cancelApplication(applicationId)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application cancelled successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to cancel application"))
        }
    }
    
    @GetMapping
    fun getMyApplications(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = bloggerApplicationService.getMyApplications(page, size)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
        }
    }
    
    @GetMapping("/{applicationId}")
    fun getApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.getApplication(applicationId)
            return ResponseEntity.ok(CommonResponse.success(application))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get application"))
        }
    }
}
