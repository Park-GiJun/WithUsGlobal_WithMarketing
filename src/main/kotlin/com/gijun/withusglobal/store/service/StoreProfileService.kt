package com.gijun.withusglobal.store.service

import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.common.service.AuthService
import com.gijun.withusglobal.store.domain.Store
import com.gijun.withusglobal.store.dto.StoreDTO
import com.gijun.withusglobal.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreProfileService(
    private val authService: AuthService,
    private val storeRepository: StoreRepository
) {
    
    fun getCurrentStore(): Store {
        val currentUser = authService.getCurrentUser()
        
        if (currentUser.getRole() != User.Role.STORE) {
            throw IllegalStateException("Current user is not a store owner")
        }
        
        return storeRepository.findByUser(currentUser)
            .orElseThrow { IllegalStateException("Store profile not found for current user") }
    }
    
    @Transactional
    fun updateProfile(request: StoreDTO.Request): Store {
        val store = getCurrentStore()
        
        // Check if new business registration number is already used by another store
        if (store.businessRegistrationNumber != request.businessRegistrationNumber && 
            storeRepository.existsByBusinessRegistrationNumber(request.businessRegistrationNumber)) {
            throw IllegalArgumentException("Business registration number is already in use")
        }
        
        store.update(
            businessName = request.businessName,
            address = request.address,
            detailAddress = request.detailAddress,
            category = request.category,
            description = request.description,
            phoneNumber = request.phoneNumber,
            websiteUrl = request.websiteUrl
        )
        
        return storeRepository.save(store)
    }
    
    fun getProfile(): Store {
        return getCurrentStore()
    }
}
