package com.github.kimleepark2.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.NotBlank

data class PutPasswordRequest(
    @Schema(description = "사용자 번호는 4~16자 이내로 설정해주세요.")
    @NotBlank(message = "사용자 번호를 입력해주세요.")
    @Length(min = 0, message = "사용자 번호를 입력해주세요.")
    val id: Long,

    @Schema(description = "변경할 비밀번호는 4~20자 이내로 설정해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 설정해주세요.")
    val password: String
)
