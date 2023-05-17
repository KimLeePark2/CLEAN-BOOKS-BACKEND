package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.exception.MyEntityNotFoundException
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    fun isNonExistEmail(email: String?): Boolean {
        if (email != null) {
            return userRepository.findByEmail(email) == null
        }
        return false
    }

    fun createMemberKakao(providerId: String?, email: String, nickname: String) {
        val user = User(
            email = email,
            password = passwordEncoder.encode("1234"),
            name = nickname,
            nickname = nickname.substring(0, nickname.let {
                if (it.length < 19) it.length else 19
            }),
            profileImagePath = "",
            role = UserRoleType.ROLE_USER,
            provider = OAuth2Provider.KAKAO,
            providerId = providerId,
        )
        userRepository.save(user)
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return null
//        return userQueryRepository.findByUsername(username) ?: throw AccountException("불가능한 계정입니다.")
//        return userQueryRepository.findByUsername(username) ?: throw AccountException("불가능한 계정입니다.")
    }

    fun oauthLogin(provider: OAuth2Provider, providerId: String?, email: String) {
        val user = userRepository.findByEmail(email) ?: throw MyEntityNotFoundException("계정을 찾을 수 없습니다.")
        user.login()
        if (providerId != null && user.providerId == null || user.providerId != providerId) {
            user.updateProvider(provider, providerId!!)
        }
        userRepository.save(user)
    }
}
