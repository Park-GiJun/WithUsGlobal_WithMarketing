package com.gijun.withusglobal.common.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val name: String,
    val role: String
)
