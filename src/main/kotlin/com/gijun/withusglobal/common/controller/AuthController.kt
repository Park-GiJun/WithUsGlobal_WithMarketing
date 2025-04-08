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
@Tag(name = "인증", description = "인증 관리 API")
class AuthController(
    private val authService: AuthService
) {
    
    @Operation(
        summary = "새 사용자 등록",
        description = "지정된 역할로 새 사용자 계정을 생성합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 등록 성공",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 이미 사용 중인 이메일"
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
        summary = "사용자 로그인",
        description = "사용자 인증 후 JWT 토큰을 반환합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 인증 정보"
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
        summary = "스토어 프로필 생성",
        description = "등록 후 스토어 소유자를 위한 프로필을 생성합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "스토어 프로필 생성 성공",
                content = [Content(schema = Schema(implementation = StoreDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 스토어 사용자가 아님"
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
        summary = "블로거 프로필 생성",
        description = "등록 후 블로거를 위한 프로필을 생성합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "블로거 프로필 생성 성공",
                content = [Content(schema = Schema(implementation = BloggerDTO.Response::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 입력값 또는 블로거 사용자가 아님"
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
        summary = "비밀번호 변경",
        description = "현재 사용자의 비밀번호를 변경합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "비밀번호 변경 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "현재 비밀번호가 잘못되었거나 필수 필드 누락"
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
