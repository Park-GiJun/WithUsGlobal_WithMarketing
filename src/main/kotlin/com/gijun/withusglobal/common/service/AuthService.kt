package com.gijun.withusglobal.common.service

import com.gijun.withusglobal.blogger.domain.Blogger
import com.gijun.withusglobal.blogger.repository.BloggerRepository
import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.common.dto.AuthResponse
import com.gijun.withusglobal.common.dto.LoginRequest
import com.gijun.withusglobal.common.dto.SignupRequest
import com.gijun.withusglobal.common.kafka.KafkaTopics
import com.gijun.withusglobal.common.kafka.message.NotificationMessage
import com.gijun.withusglobal.common.kafka.producer.KafkaProducer
import com.gijun.withusglobal.common.repository.UserRepository
import com.gijun.withusglobal.common.security.JwtTokenProvider
import com.gijun.withusglobal.store.domain.Store
import com.gijun.withusglobal.store.repository.StoreRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val bloggerRepository: BloggerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val kafkaProducer: KafkaProducer
) {
    @Transactional
    fun signup(request: SignupRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        val role = try {
            User.Role.valueOf(request.role.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid role specified")
        }
        
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            role = role
        )
        
        return userRepository.save(user)
    }
    
    @Transactional
    fun registerStore(user: User, storeRequest: Store): Store {
        if (storeRepository.findByUser(user).isPresent) {
            throw IllegalArgumentException("Store profile already exists for this user")
        }
        
        if (storeRepository.existsByBusinessRegistrationNumber(storeRequest.businessRegistrationNumber)) {
            throw IllegalArgumentException("Business registration number is already registered")
        }
        
        val store = Store(
            user = user,
            businessName = storeRequest.businessName,
            businessRegistrationNumber = storeRequest.businessRegistrationNumber,
            address = storeRequest.address,
            detailAddress = storeRequest.detailAddress,
            category = storeRequest.category,
            description = storeRequest.description,
            phoneNumber = storeRequest.phoneNumber,
            websiteUrl = storeRequest.websiteUrl
        )
        
        return storeRepository.save(store)
    }
    
    @Transactional
    fun registerBlogger(user: User, bloggerRequest: Blogger): Blogger {
        if (bloggerRepository.findByUser(user).isPresent) {
            throw IllegalArgumentException("Blogger profile already exists for this user")
        }
        
        if (bloggerRepository.existsByBlogUrl(bloggerRequest.blogUrl)) {
            throw IllegalArgumentException("Blog URL is already registered")
        }
        
        val blogger = Blogger(
            user = user,
            blogUrl = bloggerRequest.blogUrl,
            blogPlatform = bloggerRequest.blogPlatform,
            followerCount = bloggerRequest.followerCount,
            monthlyVisitors = bloggerRequest.monthlyVisitors,
            interests = bloggerRequest.interests.toMutableSet(),
            introduction = bloggerRequest.introduction,
            profileImageUrl = bloggerRequest.profileImageUrl
        )
        
        return bloggerRepository.save(blogger)
    }
    
    fun login(request: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        val user = authentication.principal as User
        val roles = user.authorities.map { it.authority.substring(5) } // Remove "ROLE_" prefix
        
        val token = jwtTokenProvider.generateToken(user.username, roles)
        
        return AuthResponse(
            token = token,
            userId = user.id!!,
            email = user.username,
            name = user.getName(),
            role = user.getRole().name
        )
    }
    
    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByEmail(authentication.name)
            .orElseThrow { IllegalStateException("Current user not found") }
    }
    
    @Transactional
    fun changePassword(currentPassword: String, newPassword: String): Boolean {
        val user = getCurrentUser()
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.password)) {
            return false
        }
        
        user.updatePassword(passwordEncoder.encode(newPassword))
        userRepository.save(user)
        
        // Send notification
        val notificationMessage = NotificationMessage(
            type = NotificationMessage.NotificationType.SYSTEM_ANNOUNCEMENT,
            recipientId = user.id!!,
            recipientRole = user.getRole().name,
            title = "Password Updated",
            message = "Your password has been successfully updated."
        )
        
        kafkaProducer.sendNotificationMessage(KafkaTopics.NOTIFICATION_CAMPAIGN, notificationMessage)
        
        return true
    }
}
