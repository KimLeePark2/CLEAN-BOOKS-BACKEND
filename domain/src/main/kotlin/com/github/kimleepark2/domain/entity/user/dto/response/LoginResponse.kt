package com.github.kimleepark2.domain.entity.user.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType
import io.swagger.v3.oas.annotations.media.Schema

data class LoginResponse(
    @Schema(description = "사용자 번호")
    val userId: String,

    @Schema(description = "사용자 로그인 이메일")
    val username: String,

    @Schema(description = "사용자 이름")
    val name: String,

    @Schema(description = "사용자 권한")
    val role: UserRoleType,

    @Schema(description = "access token, request header authorization type, Request 시 헤더에 Authorization : {tokenType} {accessToken} 으로 전달")
    val accessToken: String,

    @Schema(description = "refresh token, access token 만료 시 refresh token 으로 재발급")
    val refreshToken: String,

    @Schema(description = "request header authorization type, Request 시 헤더에 Authorization : {tokenType} {accessToken} 으로 전달")
    val tokenType: String = "Bearer ",

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "사용자 처음 로그인 여부, true라면 비밀번호 변경 필요")
    var isFirst: Boolean? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "사용자 비밀번호 변경 유무, true라면 비밀번호 변경 필요")
    var changePassword: Boolean? = null,
) {
    init {
        if (changePassword == false) {
            changePassword = null
        }
        if (isFirst == false) {
            isFirst = null
        }
    }
}
