package com.github.kimleepark2.domain.entity.user.dto.request

import com.github.kimleepark2.common.util.EnumValidation
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class UserCreateRequest(
//    @Schema(description = "계정 아이디, 아이디는 4~16자 이내로 설정해주세요.")
//    @NotBlank(message = "아이디를 입력해주세요.")
//    @Length(min = 4, max = 16, message = "아이디는 4~16자 이내로 설정해주세요.")
//    val username: String,

    @Schema(description = "사용자 닉네임", required = true)
    @NotBlank(message = "닉네임을 입력해주세요.")
    val nickname: String,

    @Schema(description = "사용자 OAuth2 로그인 제공자, LOCAL=서비스 자체 회원가입, KAKAO=카카오 로그인 계정", required = true)
    @EnumValidation(enumClass = OAuth2Provider::class, message = "유효하지 않은 OAuth2 제공자입니다.")
    val provider: OAuth2Provider,

    @Schema(description = "사용자 OAuth2 로그인 제공자", required = false)
    val providerId: String,

    @Schema(description = "사용자 이름", required = false)
    val name: String?,

    @Schema(description = "사용자 연락처", required = false)
    val phone: String?,

    @Schema(description = "사용자 이메일, LOCAL 유저 가입 시 아이디로 사용 및 OAUTH2 유저의 이메일 저장", required = false)
    val email: String?,
)
