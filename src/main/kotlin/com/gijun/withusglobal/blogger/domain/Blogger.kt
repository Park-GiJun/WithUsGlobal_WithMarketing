package com.gijun.withusglobal.blogger.domain

import com.gijun.withusglobal.common.domain.BaseEntity
import com.gijun.withusglobal.common.domain.User
import jakarta.persistence.*

@Entity
@Table(name = "bloggers")
class Blogger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,
    
    @Column(nullable = false)
    var blogUrl: String,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var blogPlatform: BlogPlatform,
    
    @Column
    var followerCount: Int? = null,
    
    @Column
    var monthlyVisitors: Int? = null,
    
    @ElementCollection
    @CollectionTable(name = "blogger_interests", joinColumns = [JoinColumn(name = "blogger_id")])
    @Column(name = "interest")
    var interests: MutableSet<String> = mutableSetOf(),
    
    @Column
    var introduction: String? = null,
    
    @Column
    var profileImageUrl: String? = null
) : BaseEntity() {
    
    enum class BlogPlatform {
        NAVER, TISTORY, BRUNCH, VELOG, MEDIUM, OTHER
    }
    
    fun update(
        blogUrl: String,
        blogPlatform: BlogPlatform,
        followerCount: Int?,
        monthlyVisitors: Int?,
        interests: Set<String>,
        introduction: String?,
        profileImageUrl: String?
    ) {
        this.blogUrl = blogUrl
        this.blogPlatform = blogPlatform
        this.followerCount = followerCount
        this.monthlyVisitors = monthlyVisitors
        this.interests.clear()
        this.interests.addAll(interests)
        this.introduction = introduction
        this.profileImageUrl = profileImageUrl
    }
}
