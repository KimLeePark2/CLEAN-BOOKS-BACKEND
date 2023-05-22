package com.github.kimleepark2.api.rest

import com.github.kimleepark2.domain.entity.product.ProductService
import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "100. 상품", description = "상품 관리 API")
class ProductRest(
    private val productService: ProductService,
) {

    @ApiResponses(
        ApiResponse(responseCode = "201", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 등록",
        description = "사용자가 판매하고자 하는 상품(책)을 등록한다."
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createUser(
        @ModelAttribute userCreateRequest: ProductCreateRequest,
    ): ProductResponse {
        return productService.createProduct(userCreateRequest)
    }
}