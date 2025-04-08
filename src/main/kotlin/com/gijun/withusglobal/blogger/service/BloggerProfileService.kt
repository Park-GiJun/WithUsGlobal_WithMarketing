package com.gijun.withusglobal.blogger.service

import com.gijun.withusglobal.blogger.domain.Blogger
import com.gijun.withusglobal.blogger.dto.BloggerDTO
import com.gijun.withusglobal.blogger.repository.BloggerRepository
import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.common.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BloggerProfileService(
    private val authService: AuthService,
    private val bloggerRepository: BloggerRepository
) {
    
    fun getCurrentBlogger(): Blogger {
        val currentUser = authService.getCurrentUser()
        
        if (currentUser.getRole() != User.Role.BLOGGER) {
            throw IllegalStateException("Current user is not a blogger")
        }
        
        return bloggerRepository.findByUser(currentUser)
            .orElseThrow { IllegalStateException("Blogger profile not found for current user") }
    }
    
    @Transactional
    fun updateProfile(request: BloggerDTO.Request): Blogger {
        val blogger = getCurrentBlogger()
        
        // Check if new blog URL is already used by another blogger
        if (blogger.blogUrl != request.blogUrl && 
            bloggerRepository.existsByBlogUrl(request.blogUrl)) {
            throw IllegalArgumentException("Blog URL is already in use")
        }
        
        blogger.update(
            blogUrl = request.blogUrl,
            blogPlatform = Blogger.BlogPlatform.valueOf(request.blogPlatform),
            followerCount = request.followerCount,
            monthlyVisitors = request.monthlyVisitors,
            interests = request.interests,
            introduction = request.introduction,
            profileImageUrl = request.profileImageUrl
        )
        
        return bloggerRepository.save(blogger)
    }
    
    fun getProfile(): Blogger {
        return getCurrentBlogger()
    }
}
