package com.gijun.withusglobal.blogger.dto

import com.gijun.withusglobal.blogger.domain.Blogger

class BloggerDTO {
    data class Request(
        val blogUrl: String,
        val blogPlatform: String,
        val followerCount: Int?,
        val monthlyVisitors: Int?,
        val interests: Set<String>,
        val introduction: String?,
        val profileImageUrl: String?
    )
    
    data class Response(
        val id: Long?,
        val userId: Long?,
        val email: String,
        val name: String,
        val blogUrl: String,
        val blogPlatform: String,
        val followerCount: Int?,
        val monthlyVisitors: Int?,
        val interests: Set<String>,
        val introduction: String?,
        val profileImageUrl: String?
    ) {
        companion object {
            fun fromEntity(blogger: Blogger): Response {
                return Response(
                    id = blogger.id,
                    userId = blogger.user.id,
                    email = blogger.user.username,
                    name = blogger.user.getName(),
                    blogUrl = blogger.blogUrl,
                    blogPlatform = blogger.blogPlatform.name,
                    followerCount = blogger.followerCount,
                    monthlyVisitors = blogger.monthlyVisitors,
                    interests = blogger.interests,
                    introduction = blogger.introduction,
                    profileImageUrl = blogger.profileImageUrl
                )
            }
        }
    }
}
