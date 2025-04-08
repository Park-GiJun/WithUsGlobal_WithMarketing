package com.gijun.withusglobal.common.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(unique = true, nullable = false)
    private val email: String,
    
    @Column(nullable = false)
    private var password: String,
    
    @Column(nullable = false)
    private val name: String,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private val role: Role,
    
    @Column(nullable = false)
    private val active: Boolean = true
) : BaseEntity(), UserDetails {

    enum class Role {
        BLOGGER, STORE, ADMIN
    }
    
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }
    
    override fun getPassword(): String = password
    
    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }
    
    override fun getUsername(): String = email
    
    fun getName(): String = name
    
    fun getRole(): Role = role
    
    override fun isAccountNonExpired(): Boolean = active
    
    override fun isAccountNonLocked(): Boolean = active
    
    override fun isCredentialsNonExpired(): Boolean = active
    
    override fun isEnabled(): Boolean = active
}
