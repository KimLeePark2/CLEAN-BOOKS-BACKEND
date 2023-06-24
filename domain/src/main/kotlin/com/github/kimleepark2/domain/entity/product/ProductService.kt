package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {
    fun save(userId: String, productCreateRequest: ProductCreateRequest): Long
    fun update(id: Long, productUpdateRequest: ProductUpdateRequest): Long
    fun sold(productId: Long, userId: String): Boolean
    fun sale(productId: Long, userId: String): Boolean
    fun wish(id: Long, userId: String): Boolean
    fun deleteById(id: Long)
    fun getById(id: Long): ProductResponse
    fun page(productPageRequest: ProductPageRequest, pageable: Pageable): Page<ProductResponse>
    fun userSales(userId: String, pageable: Pageable): Page<ProductResponse>
    fun userWishes(userId: String, pageable: Pageable): Page<ProductResponse>
}