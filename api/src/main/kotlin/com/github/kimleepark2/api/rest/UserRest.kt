package com.github.kimleepark2.api.rest

import com.github.kimleepark2.domain.entity.user.UserService
import com.github.kimleepark2.domain.entity.user.UserServiceImpl
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "000. 사용자", description = "사용자 관리 API")
class UserRest(
    private val userService: UserService,
) {
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @Operation(
        summary = "테스트 사용자로 로그인하기",
        description = "테스트 사용자 권한 받기"
    )
    @PostMapping("/test/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun testLogin(): LoginResponse {
        return userService.testLogin()
    }

//    @ApiResponses(
//        ApiResponse(responseCode = "200", description = "성공"),
//        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
//    )
//    @Operation(
//        summary = "사용자 등록",
//        description = "OAuth2 사용자 정보로 등록"
//    )
//    @PostMapping("")
//       @ResponseBody
//       @ResponseStatus(HttpStatus.CREATED)
//    fun createUser(@RequestBody userCreateRequest: UserCreateRequest): UserResponse{
//
//    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @Operation(
        summary = "로그인 사용자 정보 조회",
        description = "JWT 토큰으로 로그인 사용자 정보 조회"
    )
    @GetMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun getMyInfo(): UserResponse {
        return UserResponse(UserServiceImpl.getAccountFromSecurityContext())
    }
}