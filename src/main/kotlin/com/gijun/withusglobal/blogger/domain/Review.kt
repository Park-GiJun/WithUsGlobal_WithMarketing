package com.gijun.withusglobal.blogger.domain

import com.gijun.withusglobal.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "reviews")
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    val application: Application,
    
    @Column(nullable = false)
    var postUrl: String,
    
    @Column
    var title: String? = null,
    
    @Column(columnDefinition = "TEXT")
    var content: String? = null,
    
    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = [JoinColumn(name = "review_id")])
    @Column(name = "image_url")
    var imageUrls: MutableList<String> = mutableListOf(),
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReviewStatus = ReviewStatus.SUBMITTED,
    
    @Column
    var rejectionReason: String? = null,
    
    @Column
    var adminFeedback: String? = null
) : BaseEntity() {
    enum class ReviewStatus {
        SUBMITTED,    // Initial state when blogger submits
        PENDING,      // Under review by admin
        APPROVED,     // Review accepted
        REJECTED,     // Review rejected, needs revision
        REVISED       // Blogger has revised and resubmitted
    }
    
    fun update(
        postUrl: String,
        title: String?,
        content: String?,
        imageUrls: List<String>
    ) {
        this.postUrl = postUrl
        this.title = title
        this.content = content
        this.imageUrls.clear()
        this.imageUrls.addAll(imageUrls)
        
        // If previously rejected and now updated, change to revised
        if (status == ReviewStatus.REJECTED) {
            status = ReviewStatus.REVISED
        }
    }
    
    fun markAsPending() {
        status = ReviewStatus.PENDING
    }
    
    fun approve(feedback: String? = null) {
        status = ReviewStatus.APPROVED
        adminFeedback = feedback
        application.complete()
    }
    
    fun reject(reason: String) {
        status = ReviewStatus.REJECTED
        rejectionReason = reason
    }
}
