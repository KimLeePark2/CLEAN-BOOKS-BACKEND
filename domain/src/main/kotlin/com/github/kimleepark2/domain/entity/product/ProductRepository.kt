package com.github.kimleepark2.domain.entity.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun existsByTitle(title: String): Boolean
    fun findByTitle(title: String): Product?
    fun findAllBy(): List<Product>
    fun findByIdAndSellerId(id: Long, sellerId: String): Product?

    // var thumbnailImagePaths: List<String> 로 선언되어있는 컬럼에서
    // 단 하나의 thumbnailImagePath가 일치하는 경우를 찾기 위한 메소드
    fun findByFilesPath(thumbnailImagePath: String): Product?
}