package com.gijun.withusglobal.blogger.repository

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByApplication(application: Application): Optional<Review>
    
    fun findByStatus(status: Review.ReviewStatus, pageable: Pageable): Page<Review>
    
    @Query("SELECT r FROM Review r JOIN r.application a JOIN a.campaign c WHERE c.store.id = :storeId")
    fun findByStoreId(storeId: Long, pageable: Pageable): Page<Review>
    
    @Query("SELECT r FROM Review r JOIN r.application a JOIN a.blogger b WHERE b.id = :bloggerId")
    fun findByBloggerId(bloggerId: Long, pageable: Pageable): Page<Review>
    
    @Query("SELECT r FROM Review r JOIN r.application a JOIN a.campaign c WHERE c.id = :campaignId")
    fun findByCampaignId(campaignId: Long, pageable: Pageable): Page<Review>
}
