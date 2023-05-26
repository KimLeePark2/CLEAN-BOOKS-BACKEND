package com.github.kimleepark2.domain.entity.user.dto.response

import com.github.kimleepark2.domain.entity.user.User
import io.swagger.v3.oas.annotations.media.Schema

data class SellerResponse(
    @Schema(description = "기본키")
    val id: Long,

    @Schema(description = "사용자 계정(아이디)")
    val username: String,

    @Schema(description = "사용자 이름")
    val name: String,

    @Schema(description = "사용자 닉네임")
    val nickname: String,
) {
    constructor(user: User) : this(
        id = user.id!!,
        username = user.username,
        name = user.name,
        nickname = user.nickname,
    )
}