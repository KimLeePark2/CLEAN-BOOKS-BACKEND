package com.github.kimleepark2.api.rest

import com.github.kimleepark2.common.paging.PageRequest
import com.github.kimleepark2.domain.entity.product.ProductService
import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.github.kimleepark2.domain.entity.user.UserServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "200. 상품", description = "상품 관리 API")
class ProductRest(
    private val productService: ProductService,
) {
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 등록",
        description = "사용자가 판매하고자 하는 상품(책)을 등록한다. 파일이 포함되어있어 Formdata로 보내야 한다."
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createProduct(
        @ModelAttribute userCreateRequest: ProductCreateRequest,
    ): Long {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        return productService.save(userId, userCreateRequest)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 수정",
        description = "사용자가 판매하고자 하는 상품(책)을 수정한다. 파일이 포함되어있어 Formdata로 보내야 한다."
    )
    @PatchMapping(
        path = ["/{productId}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun updateProduct(
        @PathVariable productId: Long,
        @ModelAttribute productUpdateRequest: ProductUpdateRequest,
    ): Long {
        return productService.update(productId, productUpdateRequest)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 판매완료 처리",
        description = "상품을 판매완료 처리한다"
    )
    @PatchMapping("/{productId}/sold")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun updateProductSold(
        @PathVariable productId: Long,
    ): Boolean {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        return productService.sold(productId, userId)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 판매중 처리",
        description = "상품을 판매중 처리한다"
    )
    @PatchMapping("/{productId}/sale")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun updateProductSale(
        @PathVariable productId: Long,
    ): Boolean {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        return productService.sale(productId, userId)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 찜 토글처리",
        description = "상품을 찜(즐겨찾기) 등록/취소한다."
    )
    @PatchMapping("/{productId}/wish")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun updateProductWish(
        @PathVariable productId: Long,
    ): Boolean {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        return productService.wish(productId, userId)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 단일 상세조회",
        description = "상품을 페이지네이션 처리된 리스트를 조회한다."
    )
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getProducts(
        @PathVariable productId: Long,
    ): ProductResponse {
        return productService.getById(productId)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "상품 페이지 조회",
        description = "상품을 페이지네이션 처리된 리스트를 조회한다."
    )
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun pageProducts(
        @ParameterObject productPageRequest: ProductPageRequest,
        @ParameterObject pageRequest: PageRequest,
    ): Page<ProductResponse> {
        val pageable = pageRequest.of()
        return productService.page(productPageRequest, pageable)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "내 판매목록 리스트 조회",
        description = "사용자의 판매목록을 전체 조회한다."
    )
    @GetMapping("/{userId}/sales")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUserSales(
        @PathVariable userId: String,
        @ParameterObject pageRequest: PageRequest,
    ): Page<ProductResponse> {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        val pageable = pageRequest.of()
        return productService.userSales(userId, pageable)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "사용자 찜 리스트 조회",
        description = "사용자의 찜 리스트를 전체 조회한다."
    )
    @GetMapping("/{userId}/wishes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getUserWishes(
        @PathVariable userId: String,
        @ParameterObject pageRequest: PageRequest,
    ): Page<ProductResponse> {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val userId = loginUser.id
        val pageable = pageRequest.of()
        return productService.userWishes(userId, pageable)
    }
}