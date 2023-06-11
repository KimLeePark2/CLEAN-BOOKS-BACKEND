package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): User?
    fun findByProviderAndProviderId(provider: OAuth2Provider, providerId: String): User?
    fun findAllBy(): List<UserResponse>
}