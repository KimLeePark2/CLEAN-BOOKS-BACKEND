package com.github.kimleepark2.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.NotBlank

data class UserCreateRequest(
    @Schema(description = "계정 아이디, 아이디는 4~16자 이내로 설정해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min = 4, max = 16, message = "아이디는 4~16자 이내로 설정해주세요.")
    val username: String,

    @Schema(description = "계정 비밀번호, 비밀번호는 4~20자 이내로 설정해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 설정해주세요.")
    val password: String,

    @Schema(description = "사용자 이름")
    @NotBlank(message = "이름을 입력해주세요.")
    @Length(min = 2, max = 50, message = "이름은 최대 50자 입니다.")
    val name: String,

    @Schema(
        description = "권한은 형식을 맞춰주세요.",
        allowableValues = ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"]
    )
    val role: UserRoleType,

    @Schema(description = "구분자로 사이에 -를 입력해주세요.")
    val phone: String,
)
