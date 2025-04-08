package com.gijun.withusglobal.blogger.domain

import com.gijun.withusglobal.common.domain.BaseEntity
import com.gijun.withusglobal.store.domain.Campaign
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "applications", 
       uniqueConstraints = [UniqueConstraint(columnNames = ["campaign_id", "blogger_id"])])
class Application(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    val campaign: Campaign,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blogger_id", nullable = false)
    val blogger: Blogger,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var motivation: String,
    
    @Column
    var additionalInfo: String? = null,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ApplicationStatus = ApplicationStatus.PENDING,
    
    @Column
    var rejectionReason: String? = null,
    
    @Column
    var approvedAt: LocalDateTime? = null,
    
    @Column
    var rejectedAt: LocalDateTime? = null
) : BaseEntity() {
    enum class ApplicationStatus {
        PENDING,    // Initial state
        APPROVED,   // Selected for the campaign
        REJECTED,   // Not selected for the campaign
        CANCELLED,  // Blogger cancelled the application
        COMPLETED,  // Experience and review completed
        NO_SHOW     // Blogger didn't show up for the experience
    }
    
    fun approve() {
        if (status == ApplicationStatus.PENDING) {
            status = ApplicationStatus.APPROVED
            approvedAt = LocalDateTime.now()
            campaign.decrementRemainingSlots()
        } else {
            throw IllegalStateException("Cannot approve application in status: $status")
        }
    }
    
    fun reject(reason: String) {
        if (status == ApplicationStatus.PENDING) {
            status = ApplicationStatus.REJECTED
            rejectionReason = reason
            rejectedAt = LocalDateTime.now()
        } else {
            throw IllegalStateException("Cannot reject application in status: $status")
        }
    }
    
    fun cancel() {
        if (status == ApplicationStatus.PENDING || status == ApplicationStatus.APPROVED) {
            val previousStatus = status
            status = ApplicationStatus.CANCELLED
            
            // Only increment slot if it was previously approved
            if (previousStatus == ApplicationStatus.APPROVED) {
                campaign.incrementRemainingSlots()
            }
        } else {
            throw IllegalStateException("Cannot cancel application in status: $status")
        }
    }
    
    fun markAsNoShow() {
        if (status == ApplicationStatus.APPROVED) {
            status = ApplicationStatus.NO_SHOW
        } else {
            throw IllegalStateException("Cannot mark as no-show for application in status: $status")
        }
    }
    
    fun complete() {
        if (status == ApplicationStatus.APPROVED) {
            status = ApplicationStatus.COMPLETED
        } else {
            throw IllegalStateException("Cannot complete application in status: $status")
        }
    }
}
