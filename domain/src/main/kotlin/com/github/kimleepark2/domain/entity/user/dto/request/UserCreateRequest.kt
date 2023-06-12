package com.github.kimleepark2.domain.entity.user.dto.request

import com.github.kimleepark2.common.util.EnumValidation
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class UserCreateRequest(
//    @Schema(description = "계정 아이디, 아이디는 4~16자 이내로 설정해주세요.")
//    @NotBlank(message = "아이디를 입력해주세요.")
//    @Length(min = 4, max = 16, message = "아이디는 4~16자 이내로 설정해주세요.")
//    val username: String,

    @Schema(description = "사용자 닉네임", required = true)
    @NotBlank(message = "닉네임을 입력해주세요.")
    val nickname: String,

    @Schema(description = "사용자 OAuth2 로그인 제공자", required = true)
    @EnumValidation(enumClass = OAuth2Provider::class, message = "유효하지 않은 OAuth2 제공자입니다.")
    val provider: OAuth2Provider,

    @Schema(description = "사용자 OAuth2 로그인 제공자", required = true)
    @NotBlank(message = "OAuth2 개인 인증키를 입력해주세요.")
    val providerId: String,

    @Schema(description = "사용자 이름", required = false)
    val name: String,

    @Schema(description = "사용자 연락처", required = false)
    val phone: String,
)
