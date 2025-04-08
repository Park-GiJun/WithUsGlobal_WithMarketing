package com.gijun.withusglobal.blogger.repository

import com.gijun.withusglobal.blogger.domain.Application
import com.gijun.withusglobal.blogger.domain.Blogger
import com.gijun.withusglobal.store.domain.Campaign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ApplicationRepository : JpaRepository<Application, Long> {
    fun findByCampaignAndBlogger(campaign: Campaign, blogger: Blogger): Optional<Application>
    
    fun findByBlogger(blogger: Blogger, pageable: Pageable): Page<Application>
    
    fun findByCampaign(campaign: Campaign, pageable: Pageable): Page<Application>
    
    fun countByCampaignAndStatus(campaign: Campaign, status: Application.ApplicationStatus): Long
    
    fun countByCampaign(campaign: Campaign): Long
    
    @Query("SELECT a FROM Application a WHERE a.campaign = :campaign AND a.status = :status")
    fun findByCampaignAndStatus(campaign: Campaign, status: Application.ApplicationStatus, pageable: Pageable): Page<Application>
    
    @Query("SELECT a FROM Application a JOIN a.campaign c WHERE c.store.id = :storeId")
    fun findByStoreId(storeId: Long, pageable: Pageable): Page<Application>
    
    @Query("SELECT a FROM Application a JOIN a.campaign c WHERE c.store.id = :storeId AND a.status = :status")
    fun findByStoreIdAndStatus(storeId: Long, status: Application.ApplicationStatus, pageable: Pageable): Page<Application>
}
