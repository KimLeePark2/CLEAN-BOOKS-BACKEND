package com.github.kimleepark2.domain.entity.product.dto.request

import com.github.kimleepark2.common.BaseCondition
import com.github.kimleepark2.common.util.EnumValidation
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class ProductPageRequest(
    @Schema(description = "판매자명, 값으로 시작하는 결과만 조회, 1~50자")
    val sellerName: String?,

    @Schema(description = "글 제목, 값으로 시작하는 결과만 조회, 1~50자")
    val title: String?,

    @Schema(description = "상세 설명, 어느 위치든 포함하는 결과를 조회 1~8000자")
    val description: String?,

    @Schema(description = "글 상태")
    @EnumValidation(enumClass = ProductStatus::class)
    val status: ProductStatus?,

//    @Schema(description = "최소 판매 가격, 0 ~ MAX ~ 999_999_999")
//    @Min(value = 0, message = "최소 판매 가격은 0 이상으로 입력해주세요.")
//    var minPrice: Int? = null,
//
//    @Schema(description = "최대 판매 가격, MIN ~ 999_999_999")
//    @Max(value = 999_999_999, message = "최대 판매 가격은 999_999_999 이하로 입력해주세요.")
//    var maxPrice: Int?,

    @Schema(description = "찜 필터링, true=찜 조회, false=전체조회")
    val isWish: Boolean?,

    @Schema(description = "내 판매물품 조회, true=내거만, false=전체조회")
    val isMine: Boolean?,
) : BaseCondition(){
    var sellerId: String? = null
}
