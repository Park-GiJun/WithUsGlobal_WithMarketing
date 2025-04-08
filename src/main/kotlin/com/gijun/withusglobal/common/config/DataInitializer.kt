package com.gijun.withusglobal.common.config

import com.gijun.withusglobal.common.domain.User
import com.gijun.withusglobal.common.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByEmail("admin@withusglobal.com")) {
            val adminUser = User(
                email = "admin@withusglobal.com",
                password = passwordEncoder.encode("admin1234"),
                name = "Administrator",
                role = User.Role.ADMIN
            )
            userRepository.save(adminUser)
        }
    }
}
