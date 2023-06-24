package com.github.kimleepark2.common.jwt.filter

import com.github.kimleepark2.common.jwt.JwtTokenProvider
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.IOException
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val TOKEN_PASSER: List<String> = listOf(
        "/swagger",
        "login",
    )

    @Throws(
        IOException::class,
        ServletException::class,
        SecurityException::class,
        MalformedJwtException::class,
        ExpiredJwtException::class,
        UnsupportedJwtException::class,
        IllegalArgumentException::class,
    )
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val res: HttpServletResponse = response
        res.setHeader("Access-Control-Allow-Origin", "*")
        res.setHeader("Access-Control-Allow-Methods", "*")
        res.setHeader("Access-Control-Max-Age", "3600")
        res.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization"
        )

        val requestURI = request.requestURI
        log.info("requestURI : $requestURI")

        // 토큰 확인을 통과시킬 URI인지 확인
        val token: String? = jwtTokenProvider.resolveToken(request)
        try {
            if (
                !token.isNullOrBlank() &&
                jwtTokenProvider.validateToken(token)
            ) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                log.info("authentication : $authentication")
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            SecurityContextHolder.getContext().authentication = null
        }

        chain.doFilter(request, response)
    }

    private fun checkTokenPasser(requestURI: String): Boolean {
        log.debug("[checkTokenPasser] requestURI : $requestURI")
        log.debug("[checkTokenPasser] tokenPasser : $TOKEN_PASSER")
        return TOKEN_PASSER.any {
            if (it.startsWith("/")) requestURI.startsWith(it)
            else requestURI.contains(it)
        }
    }
}
