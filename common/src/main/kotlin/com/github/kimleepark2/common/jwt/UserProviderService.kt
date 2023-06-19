package com.github.kimleepark2.common.jwt

import org.springframework.security.core.userdetails.UserDetails

interface UserProviderService {
    fun findByProviderAndProviderId(provider: String, providerId: String): UserDetails
    fun findByUserId(userId: String): UserDetails
}