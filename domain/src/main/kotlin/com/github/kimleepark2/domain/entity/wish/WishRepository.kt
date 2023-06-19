package com.github.kimleepark2.domain.entity.wish

import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface WishRepository : JpaRepository<Wish, Long> {
    fun findByProductAndUser(product: Product, user: User): Wish?
    fun findByProduct(product: Product): List<Wish>
    fun findByUser(user: User): List<Wish>
}