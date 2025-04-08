package com.gijun.withusglobal.store.repository

import com.gijun.withusglobal.store.domain.Campaign
import com.gijun.withusglobal.store.domain.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CampaignRepository : JpaRepository<Campaign, Long> {
    fun findByStore(store: Store, pageable: Pageable): Page<Campaign>
    
    fun findByStatus(status: Campaign.CampaignStatus, pageable: Pageable): Page<Campaign>
    
    @Query("SELECT c FROM Campaign c WHERE c.status = :status AND c.applicationEndDate >= :currentDate")
    fun findActiveAndAvailableCampaigns(
        status: Campaign.CampaignStatus, 
        currentDate: LocalDate, 
        pageable: Pageable
    ): Page<Campaign>
    
    @Query("SELECT c FROM Campaign c WHERE c.category IN :categories AND c.status = :status AND c.applicationEndDate >= :currentDate")
    fun findByCategoriesAndStatusAndAvailable(
        categories: List<String>, 
        status: Campaign.CampaignStatus, 
        currentDate: LocalDate, 
        pageable: Pageable
    ): Page<Campaign>
}
