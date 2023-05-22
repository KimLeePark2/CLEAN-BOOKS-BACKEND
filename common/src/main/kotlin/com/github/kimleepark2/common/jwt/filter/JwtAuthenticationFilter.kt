package com.github.kimleepark2.common.jwt.filter

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.IOException
import io.jsonwebtoken.security.SecurityException
import com.github.kimleepark2.common.jwt.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

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

        val token: String? = jwtTokenProvider.resolveToken(request)

//        val path = request.requestURI

//        log.info("token.isNullOrBlank() : ${token.isNullOrBlank()}")
//        log.info("validPaths.isNotEmpty() : ${validPaths.isNotEmpty()}")
//        log.info("validPaths.contains(path) : ${validPaths.contains(path)}\nvalidPaths : $validPaths")

        if (!token.isNullOrBlank() &&
            // 요청 경로 검증 경로 인증은 처음 인증시엔 validPaths 데이터가 없으므로 하지 않는다.
//            && validPaths.isNotEmpty()
//            // 요청 경로가 검증할 경로에 포함되어 있다면, 토큰 인증 o
//            // 요청 경로가 검증할 경로에 포함되어있지 않다면 토큰 인증 x
//            && validPaths.contains(path)
            // 토큰이 인증 가능한 토큰이면
            jwtTokenProvider.validateToken(token)
        ) {
//            log.info("[${request.method}] ${path}?${request.queryString} -: 인증 토큰 검증 성공")
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(request, response)
    }
}
