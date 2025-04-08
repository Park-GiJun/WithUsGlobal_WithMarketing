package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.dto.ApplicationDTO
import com.gijun.withusglobal.store.service.StoreApplicationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/applications")
class StoreApplicationController(
    private val storeApplicationService: StoreApplicationService
) {
    
    @GetMapping("/campaign/{campaignId}")
    fun getApplicationsForCampaign(
        @PathVariable campaignId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = storeApplicationService.getApplicationsForCampaign(campaignId, page, size, status)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
        }
    }
    
    @GetMapping
    fun getStoreApplications(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
        try {
            val applications = storeApplicationService.getStoreApplications(page, size, status)
            return ResponseEntity.ok(CommonResponse.success(applications))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
        }
    }
    
    @GetMapping("/{applicationId}")
    fun getApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.DetailedResponse>> {
        try {
            val application = storeApplicationService.getApplication(applicationId)
            return ResponseEntity.ok(CommonResponse.success(application))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get application"))
        }
    }
    
    @PostMapping("/{applicationId}/approve")
    fun approveApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = storeApplicationService.approveApplication(applicationId)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application approved successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to approve application"))
        }
    }
    
    @PostMapping("/{applicationId}/reject")
    fun rejectApplication(
        @PathVariable applicationId: Long,
        @RequestBody request: ApplicationDTO.RejectRequest
    ): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
        try {
            val application = storeApplicationService.rejectApplication(applicationId, request.reason)
            val response = ApplicationDTO.Response.fromEntity(application)
            return ResponseEntity.ok(CommonResponse.success(response, "Application rejected successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to reject application"))
        }
    }
}
