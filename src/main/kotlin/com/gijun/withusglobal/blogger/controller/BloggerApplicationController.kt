package com.gijun.withusglobal.blogger.controller

import com.gijun.withusglobal.blogger.dto.ApplicationDTO
import com.gijun.withusglobal.blogger.service.BloggerApplicationService
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
@RequestMapping("/api/blogger/applications")
@Tag(name = "Blogger Applications", description = "Campaign Application Management for Bloggers")
class BloggerApplicationController(
    private val bloggerApplicationService: BloggerApplicationService
) {
    
    @Operation(
        summary = "Apply to a campaign",
        description = "Submit an application to participate in a marketing campaign",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Application submitted successfully",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or campaign not available"
            )
        ]
    )
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
    
    @Operation(
        summary = "Cancel an application",
        description = "Cancel a previously submitted application",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Application cancelled successfully",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to cancel application - may be in wrong status or not owned by current user"
            )
        ]
    )
    @DeleteMapping("/{applicationId}")
    fun cancelApplication(
        @Parameter(description = "ID of the application to cancel") 
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.cancelApplication(applicationId)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application cancelled successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to cancel application"))
        }
    }
    
    @Operation(
        summary = "Get all applications",
        description = "Retrieve a paginated list of all applications submitted by the current blogger",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved applications",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Failed to retrieve applications"
            )
        ]
    )
    @GetMapping
    fun getMyApplications(
        @Parameter(description = "Page number (zero-based)") 
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Size of page") 
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = bloggerApplicationService.getMyApplications(page, size)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
        }
    }
    
    @Operation(
        summary = "Get application details",
        description = "Get detailed information about a specific application",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved application details",
                content = [Content(schema = Schema(implementation = ApplicationDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Application not found or not owned by current user"
            )
        ]
    )
    @GetMapping("/{applicationId}")
    fun getApplication(
        @Parameter(description = "ID of the application to retrieve")
        @PathVariable applicationId: Long
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = bloggerApplicationService.getApplication(applicationId)
            return ResponseEntity.ok(CommonResponse.success(application))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get application"))
        }
    }
}
