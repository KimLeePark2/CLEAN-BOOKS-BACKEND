package com.github.kimleepark2.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import com.github.kimleepark2.domain.entity.user.enum.UserRoleType
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.NotBlank

data class UserCreateRequest(
    @Schema(name = "계정 아이디", description = "아이디는 4~16자 이내로 설정해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min = 4, max = 16, message = "아이디는 4~16자 이내로 설정해주세요.")
    val username: String,

    @Schema(name = "계정 비밀번호", description = "비밀번호는 4~20자 이내로 설정해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 4, max = 20, message = "비밀번호는 4~20자 이내로 설정해주세요.")
    val password: String,

    @Schema(name = "이름", description = "사용자 이름")
    @NotBlank(message = "이름을 입력해주세요.")
    @Length(min = 2, max = 50, message = "이름은 최대 50자 입니다.")
    val name: String,

    @Schema(
        name = "권한",
        description = "권한은 형식을 맞춰주세요.",
        allowableValues = ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"]
    )
    val role: UserRoleType,

    @Schema(name = "연락처", description = "구분자로 사이에 -를 입력해주세요.")
//    @field:Pattern(
//        regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$",
//        message = "연락처 형식이 올바르지 않습니다."
//    )
    val phone: String,
){
    // swagger-ui에 보이지 않도록 설정
    @Schema(hidden = true)
    var encryptedPassword: String = ""
}
