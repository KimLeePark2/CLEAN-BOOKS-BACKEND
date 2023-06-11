package com.github.kimleepark2.api.rest

import com.github.kimleepark2.domain.entity.user.UserService
import com.github.kimleepark2.domain.entity.user.UserServiceImpl
import com.github.kimleepark2.domain.entity.user.dto.request.LoginRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auths")
@Tag(name = "000. 계정", description = "사용자 계정 관리 API")
class AuthRest(
    private val userService: UserService,
) {
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @Operation(
        summary = "OAuth2 사용자 정보로 로그인하기",
        description = "사용자 정보 조회"
    )
    @PostMapping("/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun testLogin(
        @RequestBody loginRequest: LoginRequest,
    ): LoginResponse {
        return userService.login(
            provider = loginRequest.provider,
            providerId = loginRequest.providerId,
        )
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @Operation(
        summary = "로그인 사용자 정보 조회",
        description = "JWT 토큰으로 로그인 사용자 정보 조회"
    )
    @GetMapping("/my")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun getMyInfo(): UserResponse {
        return UserResponse(UserServiceImpl.getAccountFromSecurityContext())
    }
}