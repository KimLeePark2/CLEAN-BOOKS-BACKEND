package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.exception.MyEntityNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository,
) : UserDetailsService {

    fun isNonExistEmail(email: String?): Boolean {
        if (email != null) {
            return userRepository.findByEmail(email) == null
        }
        return false
    }

    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findByEmail(email) ?: throw MyEntityNotFoundException("불가능한 계정입니다.")
    }

}
