package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.exception.UnauthorizedException
import com.github.kimleepark2.common.jwt.JwtTokenProvider
import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.request.RefreshTokenRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserUpdateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import com.github.kimleepark2.domain.entity.util.findByIdOrThrow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

//    @Value("\${auth.kakao.key.admin}")
//    private lateinit var adminKey: String
//
//    @Value("\${auth.kakao.logout.redirect.url}")
//    private lateinit var kakaoLogoutRedirectUrl: String

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken {
        val userPk = jwtTokenProvider.getUserPk(refreshTokenRequest.refreshToken)
        return JwtToken(jwtTokenProvider.createAccessToken(userPk), jwtTokenProvider.createRefreshToken(userPk))
    }

    override fun getUserById(id: String): UserResponse {
        return UserResponse(userRepository.findByIdOrThrow(id, "사용자를 찾을 수 없습니다."))
    }

    override fun getUserByUsername(email: String): UserResponse {
        return UserResponse(
            userRepository.findByUsername(email)
                ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        )
    }

    override fun updateUser(userUpdateRequest: UserUpdateRequest) {
        val loginUser = getAccountFromSecurityContext()

        val user = userRepository.findByIdOrThrow(userUpdateRequest.id, "사용자를 찾을 수 없습니다.")

        user.updateInfo(userUpdateRequest)

        userRepository.save(user)

        user.update(loginUser.username)
    }

    override fun deleteUser(id: String): UserResponse {
        val user = userRepository.findByIdOrThrow(id, "사용자를 찾을 수 없습니다.")

        val loginUser = getAccountFromSecurityContext()

        user.softDelete(loginUser.username)

        userRepository.save(user)

        return UserResponse(user)
    }

//    fun changePassword(putPasswordRequest: PutPasswordRequest): Boolean {
//        val loginUser = getAccountFromSecurityContext()
//
//        // 권한체크
//
//        val user = getUserByUsername(putPasswordRequest.id)
//
//        val encryptPassword = passwordEncoder.encode(putPasswordRequest.password)
//        user.changePassword(encryptPassword)
//
//        userRepository.save(user)
//
//        return true
//    }

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

    override fun testLogin(): LoginResponse {
        val testUser = userRepository.findById("1").get()
        return LoginResponse(
            username = testUser.username,
            userId = testUser.id,
            name = testUser.name,
            role = testUser.role,
            changePassword = testUser.changePassword,
            accessToken = jwtTokenProvider.createAccessToken(testUser.username),
            refreshToken = jwtTokenProvider.createRefreshToken(testUser.username),
        )
    }

    override fun saveUser(userCreateRequest: UserCreateRequest): User {
        return userRepository.save(
            User(
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                name = userCreateRequest.name,
                nickname = userCreateRequest.nickname,
                provider = userCreateRequest.provider,
                providerId = userCreateRequest.providerId,
                username = userCreateRequest.username,
            )
        )
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)

        fun getAccountFromSecurityContext(): User {
            val authentication = SecurityContextHolder.getContext().authentication
            val principal = authentication.principal
            log.info("principal : $principal")
            if (principal == "anonymousUser") throw UnauthorizedException("계정이 확인되지 않습니다. 다시 로그인 해주세요.")
            return principal as User
        }
    }
}