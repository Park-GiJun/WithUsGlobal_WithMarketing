package com.gijun.withusglobal.store.repository

import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.store.domain.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StoreRepository : JpaRepository<Store, Long> {
    fun findByUser(user: User): Optional<Store>
    fun existsByBusinessRegistrationNumber(businessRegistrationNumber: String): Boolean
}
