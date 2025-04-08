package com.gijun.withusglobal.common.controller

import com.gijun.withusglobal.blogger.domain.Blogger
import com.gijun.withusglobal.blogger.dto.BloggerDTO
import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.common.dto.AuthResponse
import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.LoginRequest
import com.gijun.withusglobal.common.dto.SignupRequest
import com.gijun.withusglobal.common.service.AuthService
import com.gijun.withusglobal.store.domain.Store
import com.gijun.withusglobal.store.dto.StoreDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignupRequest): ResponseEntity<CommonResponse<AuthResponse>> {
        try {
            val user = authService.signup(request)
            
            // Auto-login after signup
            val loginRequest = LoginRequest(request.email, request.password)
            val authResponse = authService.login(loginRequest)
            
            return ResponseEntity.ok(CommonResponse.success(authResponse, "User registered successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to register user"))
        }
    }
    
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<CommonResponse<AuthResponse>> {
        try {
            val authResponse = authService.login(request)
            return ResponseEntity.ok(CommonResponse.success(authResponse, "Login successful"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error("Invalid email or password"))
        }
    }
    
    @PostMapping("/store/profile")
    fun createStoreProfile(@RequestBody request: StoreDTO.Request): ResponseEntity<CommonResponse<StoreDTO.Response>> {
        try {
            val currentUser = authService.getCurrentUser()
            
            if (currentUser.getRole() != User.Role.STORE) {
                return ResponseEntity.badRequest().body(CommonResponse.error("Only store users can create a store profile"))
            }
            
            val store = Store(
                user = currentUser,
                businessName = request.businessName,
                businessRegistrationNumber = request.businessRegistrationNumber,
                address = request.address,
                detailAddress = request.detailAddress,
                category = request.category,
                description = request.description,
                phoneNumber = request.phoneNumber,
                websiteUrl = request.websiteUrl
            )
            
            val savedStore = authService.registerStore(currentUser, store)
            return ResponseEntity.ok(CommonResponse.success(StoreDTO.Response.fromEntity(savedStore), "Store profile created successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to create store profile"))
        }
    }
    
    @PostMapping("/blogger/profile")
    fun createBloggerProfile(@RequestBody request: BloggerDTO.Request): ResponseEntity<CommonResponse<BloggerDTO.Response>> {
        try {
            val currentUser = authService.getCurrentUser()
            
            if (currentUser.getRole() != User.Role.BLOGGER) {
                return ResponseEntity.badRequest().body(CommonResponse.error("Only blogger users can create a blogger profile"))
            }
            
            val blogger = Blogger(
                user = currentUser,
                blogUrl = request.blogUrl,
                blogPlatform = Blogger.BlogPlatform.valueOf(request.blogPlatform),
                followerCount = request.followerCount,
                monthlyVisitors = request.monthlyVisitors,
                interests = request.interests.toMutableSet(),
                introduction = request.introduction,
                profileImageUrl = request.profileImageUrl
            )
            
            val savedBlogger = authService.registerBlogger(currentUser, blogger)
            return ResponseEntity.ok(CommonResponse.success(BloggerDTO.Response.fromEntity(savedBlogger), "Blogger profile created successfully"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to create blogger profile"))
        }
    }
    
    @PostMapping("/password")
    fun changePassword(@RequestBody request: Map<String, String>): ResponseEntity<CommonResponse<Nothing>> {
        try {
            val currentPassword = request["currentPassword"]
                ?: return ResponseEntity.badRequest().body(CommonResponse.error("Current password is required"))
            val newPassword = request["newPassword"]
                ?: return ResponseEntity.badRequest().body(CommonResponse.error("New password is required"))
            
            val success = authService.changePassword(currentPassword, newPassword)
            
            return if (success) {
                ResponseEntity.ok(CommonResponse.success(null, "Password changed successfully"))
            } else {
                ResponseEntity.badRequest().body(CommonResponse.error("Current password is incorrect"))
            }
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to change password"))
        }
    }
}
