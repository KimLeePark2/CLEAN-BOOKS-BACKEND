package com.github.kimleepark2.domain.entity.product.dto.request

import com.github.kimleepark2.domain.entity.file.File
import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.user.User
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.NotBlank
import org.springframework.web.multipart.MultipartFile

// 유저 id, 글 제목, 책 제목, 출판사, 가격, 지역, 등록일, 작가 , 사진
data class ProductCreateRequest(
//    @Schema(description = "사용자 번호")
//    @Length(min = 1, message = "사용자 번호를 입력해주세요.")
//    val userId: String,

    @Schema(description = "글 제목, 1~50자")
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    @Length(min = 1, max = 50, message = "글 제목은 1~50자 이내로 입력해주세요.")
    val title: String,

    @Schema(description = "상세 설명, 1~8000자")
    @NotBlank(message = "글 제목을 입력해주세요.")
    @Length(min = 1, max = 8000, message = "글 제목은 1~8000자 이내로 입력해주세요.")
    val description: String,

    @Schema(description = "책 판매 가격, 0 ~ 999_999_999")
    @NotBlank(message = "판매가를 입력해주세요.")
    @Length(min = 0, max = 999_999_999, message = "책 판매 가격은 0 ~ 999_999_999 이내로 입력해주세요")
    val price: Int,

    @Schema(description = "책 썸네일 (MultipartFile)", defaultValue = "null")
    val thumbnailImages: List<MultipartFile>? = null,
) {

    fun toEntity(seller: User): Product = Product(
        title = title,
        description = description,
        price = price,
        seller = seller,
    )

    fun toEntity(thumbnailImages: MutableList<File>, seller: User): Product = Product(
        title = title,
        description = description,
        price = price,
        files = thumbnailImages,
        seller = seller,
    )
}
