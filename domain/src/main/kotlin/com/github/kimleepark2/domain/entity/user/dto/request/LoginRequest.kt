package com.github.kimleepark2.domain.entity.user.dto.request

import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class LoginRequest(
    @Schema(description = "OAuth2 제공자")
    @NotBlank(message = "제공자를 입력해주세요.")
    val provider: OAuth2Provider,

    @Schema(description = "OAuth2 제공자 아이디")
    @Length(min = 1, message = "OAuth2 로그인 후 받은 아이디를 입력해주세요")
    val providerId: String
)
