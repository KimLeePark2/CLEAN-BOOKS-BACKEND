package com.github.kimleepark2.domain.entity.product.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.Min
import org.springframework.web.multipart.MultipartFile

data class ProductUpdateRequest(
    @Schema(name = "계정 번호", description = "필수 입력입니다.")
    @Min(value = 1, message = "계정 번호는 1 이상 입력해주세요.")
    val id: Long,

    @Schema(description = "글 제목, 1~50자")
    @Length(min = 1, max = 50, message = "글 제목은 1~50자 이내로 입력해주세요.")
    val title: String,

    @Schema(description = "상세 설명, 1~8000자")
    @Length(min = 1, max = 8000, message = "글 제목은 1~8000자 이내로 입력해주세요.")
    val description: String,

    @Schema(description = "책 판매 가격, 0 ~ 999_999_999")
    @Length(min = 0, max = 999_999_999, message = "책 판매 가격은 0 ~ 999_999_999 이내로 입력해주세요")
    var price: Int,

    @Schema(description = "책 썸네일 (MultipartFile)", defaultValue = "null")
    var thumbnailImage: MultipartFile? = null,
)
