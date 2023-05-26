package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.exception.MyEntityNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository,
) : UserDetailsService {

    fun isNonExistUsername(username: String?): Boolean {
        if (username != null) {
            return userRepository.findByUsername(username) == null
        }
        return false
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username) ?: throw MyEntityNotFoundException("불가능한 계정입니다.")
    }
}
