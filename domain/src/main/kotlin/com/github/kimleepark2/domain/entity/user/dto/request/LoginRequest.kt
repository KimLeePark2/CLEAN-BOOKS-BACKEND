package com.github.kimleepark2.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class LoginRequest(
    @Schema(description = "아이디는 4~16자 이내로 설정해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min = 4, max = 16, message = "아이디는 4~16자 이내로 설정해주세요.")
    val username: String,

    @Schema(description = "비밀번호는 4~20자 이내로 설정해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 설정해주세요.")
    val password: String
)
