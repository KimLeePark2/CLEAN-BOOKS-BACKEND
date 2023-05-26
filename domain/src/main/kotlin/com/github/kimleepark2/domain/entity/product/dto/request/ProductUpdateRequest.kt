package com.github.kimleepark2.domain.entity.product.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.Min

data class ProductUpdateRequest(
    @Schema(name = "계정 번호", description = "필수 입력입니다.")
    @Min(value = 1, message = "계정 번호는 1 이상 입력해주세요.")
    val id: Long,

    @Schema(description = "사용자 이름")
    @Length(min = 2, message = "이름은 2자 이상 입력해주세요.")
    val name: String?,

    @Schema(description = "사용자 닉네임")
    @Length(min = 1, max = 20, message = "닉네임은 1~20자입니다.")
    val nickname: String?,

    @field:Schema(name = "연락처", description = "구분자로 사이에 -를 입력해주세요.")
//    @field:Pattern(
//        regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$",
//        message = "연락처 형식이 올바르지 않습니다."
//    )
    val phone: String?,
)
