package com.github.kimleepark2.api.config.oauth2

import com.github.kimleepark2.common.jwt.JwtTokenProvider
import com.github.kimleepark2.domain.entity.user.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
) : SimpleUrlAuthenticationSuccessHandler() {

    @Value("\${spring.profiles.active:local}")
    private val profiles: String? = null

    @Value("\${auth.kakao.login.redirect.url}")
    private val kakaoRedirectUrl: String? = null

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val oAuth2User = authentication.principal as OAuth2User

        // Kakao에서 받은 OAuth 정보 Map
        val kakaoAccount = oAuth2User.attributes["kakao_account"] as Map<*, *>
        val email = kakaoAccount["email"] as String

        val user = userService.getUserByUsername(email)
        logger.info("user : $user")

        val nickname = user.nickname

        val jwt = jwtTokenProvider.generateTokenForOAuth("kakao", email, nickname)
        val url = makeRedirectUrl(token = jwt)

        if (response.isCommitted) {
            logger.debug("응답이 이미 커밋된 상태입니다.")
            return
        }

        logger.debug("redirect-url : $url")
        redirectStrategy.sendRedirect(request, response, url)
    }

    private fun makeRedirectUrl(
        token: String,
    ): String {
        // url은 프론트 주소로 반환한다. dns 적용 후 변경예정 ?

        val path = "/kakaoLogin?token=$token"
        val url: String = kakaoRedirectUrl + path

        return UriComponentsBuilder.fromUriString(url)
            .build().toUriString()
    }
}