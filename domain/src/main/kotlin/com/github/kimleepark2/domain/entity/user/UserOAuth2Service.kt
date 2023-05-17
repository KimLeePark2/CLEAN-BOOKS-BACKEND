package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class UserOAuth2Service(
    private val userService: UserDetailServiceImpl,
) : DefaultOAuth2UserService() {
    //    private val httpSession: HttpSession? = null
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private val redirectUri: String? = null

    init {
        log.info("redirect uri : $redirectUri")
    }

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        log.info("load user")
        val oAuth2User = super.loadUser(userRequest)
        val attributes = oAuth2User.attributes
        log.info("attributes : $attributes")
        /**
         * attributes : {
         id=2550758618,
         connected_at=2022-11-26T06:51:13Z,
         properties={nickname=이성복},
         kakao_account={
         profile_nickname_needs_agreement=false,
         profile={nickname=이성복},
         has_email=true,
         email_needs_agreement=false,
         is_email_valid=true,
         is_email_verified=true,
         email=lenope1214@kakao.com
         }
         }
         */
        val providerIdValue: Any? = attributes["id"]
        val providerId: String? = providerIdValue?.toString()
        val kakaoAccount: Map<String, Any> = attributes["kakao_account"] as Map<String, Any>
        val email: String = kakaoAccount["email"] as String
        val properties: Map<String, Any> = attributes["properties"] as Map<String, Any>
        val nickname = properties["nickname"] as String
        if (userService.isNonExistEmail(email)) {
            userService.createMemberKakao(providerId, email, nickname)
        } else {
            // 로그인 처리
            userService.oauthLogin(OAuth2Provider.KAKAO, providerId, email)
            log.info("이미 존재하는 이메일입니다.")
        }
        return DefaultOAuth2User(setOf(SimpleGrantedAuthority(UserRoleType.ROLE_USER.name)), attributes, "id")
    }
}