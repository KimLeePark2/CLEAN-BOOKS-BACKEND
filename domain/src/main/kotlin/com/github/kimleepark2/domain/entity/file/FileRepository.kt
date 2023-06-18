package com.github.kimleepark2.domain.entity.file

import com.github.kimleepark2.domain.entity.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {
    fun findByPath(path: String): File?
    fun findByPathAndProduct(path: String, product: Product): File?
}