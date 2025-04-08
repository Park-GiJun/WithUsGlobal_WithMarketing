package com.gijun.withusglobal.admin.service

import com.gijun.withusglobal.admin.dto.DashboardDTO
import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.domain.Review
import com.gijun.withusglobal.blogger.repository.ApplicationRepository
import com.gijun.withusglobal.blogger.repository.BloggerRepository
import com.gijun.withusglobal.blogger.repository.ReviewRepository
import com.gijun.withusglobal.common.repository.UserRepository
import com.gijun.withusglobal.store.domain.Campaign
import com.gijun.withusglobal.store.repository.CampaignRepository
import com.gijun.withusglobal.store.repository.StoreRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AdminDashboardService(
    private val userRepository: UserRepository,
    private val bloggerRepository: BloggerRepository,
    private val storeRepository: StoreRepository,
    private val campaignRepository: CampaignRepository,
    private val applicationRepository: ApplicationRepository,
    private val reviewRepository: ReviewRepository
) {
    
    fun getDashboardSummary(): DashboardDTO.Summary {
        val totalUsers = userRepository.count().toInt()
        val totalBloggers = bloggerRepository.count().toInt()
        val totalStores = storeRepository.count().toInt()
        val totalCampaigns = campaignRepository.count().toInt()
        
        val activeCampaigns = campaignRepository.findByStatus(
            Campaign.CampaignStatus.ACTIVE, 
            PageRequest.of(0, 1)
        ).totalElements.toInt()
        
        val pendingCampaigns = campaignRepository.findByStatus(
            Campaign.CampaignStatus.PENDING, 
            PageRequest.of(0, 1)
        ).totalElements.toInt()
        
        val totalApplications = applicationRepository.count().toInt()
        
        val approvedApplications = applicationRepository.findAll()
            .count { it.status == Application.ApplicationStatus.APPROVED }.toInt()
        
        val totalReviews = reviewRepository.count().toInt()
        
        val pendingReviews = reviewRepository.findByStatus(
            Review.ReviewStatus.SUBMITTED, 
            PageRequest.of(0, 1)
        ).totalElements.toInt() +
        reviewRepository.findByStatus(
            Review.ReviewStatus.REVISED, 
            PageRequest.of(0, 1)
        ).totalElements.toInt()
        
        val approvedReviews = reviewRepository.findByStatus(
            Review.ReviewStatus.APPROVED, 
            PageRequest.of(0, 1)
        ).totalElements.toInt()
        
        return DashboardDTO.Summary(
            totalUsers = totalUsers,
            totalBloggers = totalBloggers,
            totalStores = totalStores,
            totalCampaigns = totalCampaigns,
            activeCampaigns = activeCampaigns,
            pendingCampaigns = pendingCampaigns,
            totalApplications = totalApplications,
            approvedApplications = approvedApplications,
            totalReviews = totalReviews,
            pendingReviews = pendingReviews,
            approvedReviews = approvedReviews
        )
    }
}
