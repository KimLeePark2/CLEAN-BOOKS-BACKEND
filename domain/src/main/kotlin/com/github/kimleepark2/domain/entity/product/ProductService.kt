package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {
    fun save(productCreateRequest: ProductCreateRequest): Long
    fun update(id: Long, productUpdateRequest: ProductUpdateRequest): Long

    fun deleteById(id: Long)

    fun getById(id: Long): ProductResponse

    fun page(productPageRequest: ProductPageRequest, pageable: Pageable): Page<ProductResponse>
}