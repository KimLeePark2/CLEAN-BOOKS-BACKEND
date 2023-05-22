package com.github.kimleepark2.domain.entity.user

import jakarta.persistence.LockModeType
import com.github.kimleepark2.domain.entity.user.dto.request.*
import com.github.kimleepark2.domain.entity.util.findByIdOrThrow
import com.github.kimleepark2.common.exception.UnauthorizedException
import com.github.kimleepark2.common.jwt.JwtTokenProvider
import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Lock
import org.springframework.security.authentication.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class UserServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): UserService {

//    @Value("\${auth.kakao.key.admin}")
//    private lateinit var adminKey: String
//
//    @Value("\${auth.kakao.logout.redirect.url}")
//    private lateinit var kakaoLogoutRedirectUrl: String

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken {
        val userPk = jwtTokenProvider.getUserPk(refreshTokenRequest.refreshToken)
        return JwtToken(jwtTokenProvider.createAccessToken(userPk), jwtTokenProvider.createRefreshToken(userPk))
    }

    override fun getUserById(id: Long): User {
        return userRepository.findByIdOrThrow(id, "사용자를 찾을 수 없습니다.")
    }

    override fun getLoginUserInfo(): User {
        return getAccountFromSecurityContext()
////        val newLpList = lpQueryRepository.findMyNewLpList(loginUser.id!!)
//        return UserResponse.of(loginUser)
    }

    override fun getUserByUsername(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
    }

    override fun updateUser(userUpdateRequest: UserUpdateRequest): Unit {
        val loginUser = getAccountFromSecurityContext()

        val user = getUserById(userUpdateRequest.id)

//        val laundryId = loginUser.getLaundry()?.id
//        val clientId = loginUser.getClient()?.id
//        val departmentId = loginUser.getDepartment()?.id
//        if (!loginUser.beAbleRole(laundryId, clientId, departmentId)) throw ForbiddenException()

        user.updateInfo(userUpdateRequest)

        userRepository.save(user)

        user.update(loginUser.username)
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE) // 읽기 쓰기 모두 불가능하도록 LOCK 걸기
    override fun deleteUser(id: Long): UserResponse {
        val user = getUserById(id)

        val loginUser = getAccountFromSecurityContext()

        user.softDelete(loginUser.username)

        userRepository.save(user)

        return UserResponse(user)
    }

    fun changePassword(putPasswordRequest: PutPasswordRequest): Boolean {
        val loginUser = getAccountFromSecurityContext()
        
        // 권한체크

        val user = getUserByUsername(putPasswordRequest.email)

        val encryptPassword = passwordEncoder.encode(putPasswordRequest.password)
        user.changePassword(encryptPassword)

        userRepository.save(user)

        return true
    }

    override fun kakaoLogout(): String {
//        val loginUser: User = getAccountFromSecurityContext()
//        log.info("카카오 로그아웃 - $adminKey,${loginUser.providerId!!}")
//
//        val logoutUrl: String = "https://kapi.kakao.com/v1/user/logout"
//
//        val client: HttpClient = HttpClient.newBuilder().build()
//        val request: HttpRequest = HttpRequest.newBuilder()
//            .uri(URI.create("$logoutUrl?target_id_type=user_id&target_id=${loginUser.providerId!!}"))
//            .setHeader("Authorization", "KakaoAK $adminKey")
//            .POST(HttpRequest.BodyPublishers.ofString(""))
//            .build()
//
//        log.info("request : $request")
//        request.headers().map().forEach { (key, value) -> log.info("key : $key, value : $value") }
//
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
//        log.info("${response.body()[0]} 유저 로그아웃")
        return "success"
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)

        fun getAccountFromSecurityContext(): User {
            val authentication = SecurityContextHolder.getContext().authentication
            log.info("authentication : $authentication")
            val principal = authentication.principal
            log.info("principal : $principal")
            if (principal == "anonymousUser") throw UnauthorizedException("계정이 확인되지 않습니다. 다시 로그인 해주세요.")
            return principal as User
        }
    }
}