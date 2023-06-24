package com.github.kimleepark2.domain.entity.product.dto.response

import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import com.github.kimleepark2.domain.entity.user.dto.response.SellerResponse
import com.querydsl.core.annotations.QueryProjection
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ProductResponse @QueryProjection constructor(
    @Schema(description = "상품 번호")
    val id: Long,

    @Schema(description = "글 제목")
    var title: String,

    @Schema(description = "글 설명")
    var description: String,

    @Schema(description = "글 상태")
    var status: ProductStatus = ProductStatus.SALE,

    @Schema(description = "책 가격")
    var price: Int,

    // 이 밑으론 QClass 사용 부분이라 건드리지 않는다.

    @Schema(description = "책 썸네일 경로")
    var thumbnailImagePaths: List<String>,

    @Schema(description = "책 판매자 정보")
    val seller: SellerResponse,

    @Schema(description = "책 좋아요 수")
    var wishes: Long,

    @Schema(description = "책 생성일")
    val createdAt: LocalDateTime,
) {
//    @JsonIgnore
//    @Transient
//    var files: List<File>
//
//    @Schema(description = "책 썸네일 경로")
//    var thumbnailImagePaths: List<String> = files.map { it.path }

    constructor(product: Product) : this(
        id = product.id,
        title = product.title,
        description = product.description,
        status = product.status,
        price = product.price,
        thumbnailImagePaths = product.files.map { it.path },
        seller = SellerResponse(product.seller),
        wishes = product.wishes.size.toLong(),
        createdAt = product.createdAt,
//        files = product.files,
    )
}