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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication Management API")
class AuthController(
    private val authService: AuthService
) {
    
    @Operation(
        summary = "Register a new user",
        description = "Create a new user account with specified role",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User registered successfully",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or email already in use"
            )
        ]
    )
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
    
    @Operation(
        summary = "Login user",
        description = "Authenticate user and return JWT token",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Login successful",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid credentials"
            )
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<CommonResponse<AuthResponse>> {
        try {
            val authResponse = authService.login(request)
            return ResponseEntity.ok(CommonResponse.success(authResponse, "Login successful"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error("Invalid email or password"))
        }
    }
    
    @Operation(
        summary = "Create store profile",
        description = "Create a profile for store owners after registration",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Store profile created successfully",
                content = [Content(schema = Schema(implementation = StoreDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or not a store user"
            )
        ]
    )
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
    
    @Operation(
        summary = "Create blogger profile",
        description = "Create a profile for bloggers after registration",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Blogger profile created successfully",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input or not a blogger user"
            )
        ]
    )
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
    
    @Operation(
        summary = "Change password",
        description = "Change the current user's password",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Password changed successfully"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid current password or missing required fields"
            )
        ]
    )
    @PostMapping("/password")
    fun changePassword(@RequestBody request: Map<String, String>): ResponseEntity<CommonResponse<Any>> {
        try {
            val currentPassword = request["currentPassword"]
                ?: return ResponseEntity.badRequest().body(CommonResponse.error("Current password is required"))
            val newPassword = request["newPassword"]
                ?: return ResponseEntity.badRequest().body(CommonResponse.error("New password is required"))
            
            val success = authService.changePassword(currentPassword, newPassword)
            
            return if (success) {
                ResponseEntity.ok(CommonResponse.success(mapOf("success" to true), "Password changed successfully"))
            } else {
                ResponseEntity.badRequest().body(CommonResponse.error("Current password is incorrect"))
            }
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to change password"))
        }
    }
}
