package com.gijun.withusglobal.store.domain

import com.gijun.withusglobal.common.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "campaigns")
class Campaign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,
    
    @Column(nullable = false)
    var title: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,
    
    @Column(nullable = false)
    var category: String,
    
    @Column(nullable = false)
    var totalSlots: Int,
    
    @Column(nullable = false)
    var remainingSlots: Int,
    
    @Column(nullable = false)
    var benefitDescription: String,
    
    @Column(nullable = false)
    var requiredPostType: String,
    
    @Column(nullable = false)
    var applicationStartDate: LocalDate,
    
    @Column(nullable = false)
    var applicationEndDate: LocalDate,
    
    @Column(nullable = false)
    var experienceStartDate: LocalDate,
    
    @Column(nullable = false)
    var experienceEndDate: LocalDate,
    
    @Column(nullable = false)
    var reviewDueDate: LocalDate,
    
    @ElementCollection
    @CollectionTable(name = "campaign_images", joinColumns = [JoinColumn(name = "campaign_id")])
    @Column(name = "image_url")
    var imageUrls: MutableList<String> = mutableListOf(),
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CampaignStatus = CampaignStatus.DRAFT
) : BaseEntity() {
    enum class CampaignStatus {
        DRAFT,           // Initial creation, not yet published
        PENDING,         // Submitted for review by admin
        ACTIVE,          // Published and accepting applications
        APPLICATION_CLOSED, // Not accepting new applications
        IN_PROGRESS,     // Experience stage (bloggers visiting)
        REVIEW_PERIOD,   // Waiting for bloggers to submit reviews
        COMPLETED,       // All reviews done
        CANCELLED        // Campaign cancelled
    }
    
    fun update(
        title: String,
        description: String,
        category: String,
        totalSlots: Int,
        benefitDescription: String,
        requiredPostType: String,
        applicationStartDate: LocalDate,
        applicationEndDate: LocalDate,
        experienceStartDate: LocalDate,
        experienceEndDate: LocalDate,
        reviewDueDate: LocalDate,
        imageUrls: List<String>
    ) {
        this.title = title
        this.description = description
        this.category = category
        this.totalSlots = totalSlots
        this.remainingSlots = totalSlots - (this.totalSlots - this.remainingSlots) // Keep existing allocations
        this.benefitDescription = benefitDescription
        this.requiredPostType = requiredPostType
        this.applicationStartDate = applicationStartDate
        this.applicationEndDate = applicationEndDate
        this.experienceStartDate = experienceStartDate
        this.experienceEndDate = experienceEndDate
        this.reviewDueDate = reviewDueDate
        this.imageUrls.clear()
        this.imageUrls.addAll(imageUrls)
    }
    
    fun publishCampaign() {
        if (status == CampaignStatus.DRAFT || status == CampaignStatus.PENDING) {
            status = CampaignStatus.ACTIVE
        } else {
            throw IllegalStateException("Cannot publish campaign in status: $status")
        }
    }
    
    fun closeCampaign() {
        if (status == CampaignStatus.ACTIVE) {
            status = CampaignStatus.APPLICATION_CLOSED
        } else {
            throw IllegalStateException("Cannot close campaign in status: $status")
        }
    }
    
    fun decrementRemainingSlots() {
        if (remainingSlots > 0) {
            remainingSlots--
        } else {
            throw IllegalStateException("No remaining slots available")
        }
    }
    
    fun incrementRemainingSlots() {
        if (remainingSlots < totalSlots) {
            remainingSlots++
        }
    }
}
