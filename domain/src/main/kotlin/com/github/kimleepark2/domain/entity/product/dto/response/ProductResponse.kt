package com.github.kimleepark2.domain.entity.product.dto.response

import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import com.github.kimleepark2.domain.entity.user.dto.response.SellerResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import com.querydsl.core.annotations.QueryProjection
import io.swagger.v3.oas.annotations.media.Schema

data class ProductResponse @QueryProjection constructor(
    @Schema(description = "상품 번호")
    val id: Long,

    @Schema(description = "글 제목")
    var title: String,

    @Schema(description = "글 상태")
    var status: ProductStatus = ProductStatus.SALE,

    @Schema(description = "책 제목")
    var bookTitle: String,

    @Schema(description = "책 저자")
    var author: String,

    @Schema(description = "책 가격")
    var price: Integer,

    @Schema(description = "책 출판사")
    var publisher: String,

    @Schema(description = "책 썸네일 경로")
    var thumbnailImagePath: String,

    @Schema(description = "책 판매자 정보")
    val seller: SellerResponse,

    @Schema(description = "책 좋아요 수")
    var wishes: Long,
){
    constructor(product: Product) : this(
        id = product.id!!,
        title = product.title,
        status = product.status,
        bookTitle = product.bookTitle,
        author = product.author,
        price = product.price,
        publisher = product.publisher,
        thumbnailImagePath = product.thumbnailImagePath,
        seller = SellerResponse(product.seller),
        wishes = product.wishes.size.toLong()
    )
}