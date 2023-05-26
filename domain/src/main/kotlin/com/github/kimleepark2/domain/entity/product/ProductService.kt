package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse

interface ProductService {
    fun createProduct(productCreateRequest: ProductCreateRequest): ProductResponse

    fun getById(id: Long): ProductResponse

    fun update(userUpdateRequest: ProductUpdateRequest)

    fun deleteById(id: Long): ProductResponse
}