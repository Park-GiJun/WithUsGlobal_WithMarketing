package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.store.dto.StoreDTO
import com.gijun.withusglobal.store.service.StoreProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/profile")
class StoreProfileController(
    private val storeProfileService: StoreProfileService
) {
    
    @GetMapping
    fun getProfile(): ResponseEntity<CommonResponse<StoreDTO.Response>> {
        try {
            val store = storeProfileService.getProfile()
            val response = StoreDTO.Response.fromEntity(store)
            return ResponseEntity.ok(CommonResponse.success(response))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get profile"))
        }
    }
    
    @PutMapping
    fun updateProfile(@RequestBody request: StoreDTO.Request): ResponseEntity<CommonResponse<StoreDTO.Response>> {
        try {
            val updatedStore = storeProfileService.updateProfile(request)
            val response = StoreDTO.Response.fromEntity(updatedStore)
            return ResponseEntity.ok(CommonResponse.success(response, "Profile updated successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to update profile"))
        }
    }
}
