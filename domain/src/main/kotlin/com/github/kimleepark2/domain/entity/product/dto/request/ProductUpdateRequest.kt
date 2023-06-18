package com.github.kimleepark2.domain.entity.product.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length
import org.springframework.web.multipart.MultipartFile

data class ProductUpdateRequest(

    @Schema(description = "글 제목, 1~50자")
    @Length(min = 1, max = 50, message = "글 제목은 1~50자 이내로 입력해주세요.")
    val title: String? = null,

    @Schema(description = "상세 설명, 1~8000자")
    @Length(min = 1, max = 8000, message = "글 제목은 1~8000자 이내로 입력해주세요.")
    val description: String? = null,

    @Schema(description = "책 판매 가격, 0 ~ 999_999_999")
    @Length(min = 0, max = 999_999_999, message = "책 판매 가격은 0 ~ 999_999_999 이내로 입력해주세요")
    val price: Int? = null,

    @Schema(description = "삭제할 파일 경로들, 기존 파일 수정 시에도 삭제해야 하므로 리스트에 추가해서 요청해야합니다.")
    val deleteFilePaths: List<String>? = null,

    @Schema(description = "추가/수정할 이미지 파일", defaultValue = "null")
    val thumbnailImages: List<MultipartFile>? = null,
)