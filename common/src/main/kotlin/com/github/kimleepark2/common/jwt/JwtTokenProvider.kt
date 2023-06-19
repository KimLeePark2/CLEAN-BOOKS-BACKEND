package com.github.kimleepark2.common.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import com.github.kimleepark2.common.exception.UnauthorizedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.Key
import java.util.*
import jakarta.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val userProviderService: UserProviderService,
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private var secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    // 토큰 유효시간 8시간, 재발급 테스트 중에 15초로 수정함
//    private val accessTokenValidTime = 15 * 1000L
    private val accessTokenValidTime = 24 * 60 * 60 * 1000L

    // 토큰 유효시간 30일
    private val refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L

    private fun createToken(userId: String, validTime: Long): String {
        val claims: Claims = Jwts.claims().setSubject(userId)
        claims["userId"] = userId
        val now = Date()

        val expiredTime = now.time + validTime
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(expiredTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createAccessToken(userId: String): String {
        log.info("create accessToken - userId: $userId")
        return createToken(userId, accessTokenValidTime)
    }

    fun createRefreshToken(userId: String): String {
        log.info("create accessToken - userId: $userId")
        return createToken(userId, refreshTokenValidTime)
    }

    @Transactional(readOnly = true)
    fun getAuthentication(token: String): Authentication {
        val userDetails = userProviderService.findByUserId(
            getUserId(token),
        )
        log.info("jwt get authentication - user details : {}", userDetails)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

//    fun getProvider(token: String): String {
//        val subject = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.get(
//            "provider",
//            String::class.java
//        )
//        if (subject.isBlank()) throw UnauthorizedException("Invalid JWT token, please update token")
//        return subject
//    }

    fun getUserId(token: String): String {
        val subject = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.subject
        if (subject.isBlank()) throw UnauthorizedException("Invalid JWT token, please update token")
        return subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val authorizationValue = request.getHeader("Authorization")
        log.info("resolve token : {}", authorizationValue)
        val token = authorizationValue ?: return null
        return if (token.indexOf("Bearer ") > -1) token.replace("Bearer ", "") else ""
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: SecurityException) {
            log.error("Invalid JWT security - message : ${e.message}")
            throw UnauthorizedException("Invalid JWT security, please update token")
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token - message : ${e.message}")
            throw UnauthorizedException("Invalid JWT token, please update token")
        } catch (e: ExpiredJwtException) {
            log.error("Expired JWT token - message : ${e.message}")
            throw UnauthorizedException("Expired JWT token, please update token")
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported JWT token - message : ${e.message}")
            throw UnauthorizedException("Unsupported JWT token, please update token")
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty - message : ${e.message}")
            throw UnauthorizedException("JWT claims string is empty, please update token")
        }
    }

    fun generateTokenForOAuth(type: String, email: String, nickname: String): String {
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["type"] = type
        claims["email"] = email
        claims["nickname"] = nickname
        val now = Date()
        val expiredTime = now.time + accessTokenValidTime
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(expiredTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
}
