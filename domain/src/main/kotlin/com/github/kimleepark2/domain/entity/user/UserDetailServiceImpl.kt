package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.exception.MyEntityNotFoundException
import com.github.kimleepark2.common.jwt.UserProviderService
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository,
) : UserDetailsService, UserProviderService {

    fun isNonExistUsername(username: String?): Boolean {
        if (username != null) {
            return userRepository.findByEmail(username) == null
        }
        return false
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username) ?: throw MyEntityNotFoundException("불가능한 계정입니다.")
    }

    override fun findByProviderAndProviderId(provider: String, providerId: String): UserDetails {
        return userRepository.findByProviderAndProviderId(OAuth2Provider.valueOf(provider), providerId)
            ?: throw MyEntityNotFoundException("불가능한 계정입니다.")
    }
}
