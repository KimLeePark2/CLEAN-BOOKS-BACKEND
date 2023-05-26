package com.github.kimleepark2.domain.entity.user.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import com.github.kimleepark2.domain.entity.user.User
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType

data class UserResponse(
    @Schema(description = "사용자 번호")
    val id: String,

    @Schema(description = "사용자 로그인 이메일")
    val username: String,

    @Schema(description = "사용자 이름")
    val name: String,

    @Schema(description = "사용자 닉네임")
    val nickname: String,

    @Schema(description = "사용자 권한")
    val role: UserRoleType = UserRoleType.ROLE_USER,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "사용자 비밀번호 변경 유무, true라면 비밀번호 변경 필요")
    var changePassword: Boolean? = null,
) {
    constructor(user: User, isFirst: Boolean? = null) : this(
        id = user.id!!,
        username = user.username,
        name = user.name,
        nickname = user.nickname,
        role = user.role,
        changePassword = user.changePassword
    )

    init {
        if (changePassword == false) {
            changePassword = null
        }
    }
}