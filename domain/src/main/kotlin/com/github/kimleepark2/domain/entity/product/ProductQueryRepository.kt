package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.common.paging.PaginationSortRepository
import com.github.kimleepark2.domain.entity.product.QProduct.Companion.product
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import com.github.kimleepark2.domain.entity.product.dto.response.QProductResponse
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import com.github.kimleepark2.domain.entity.user.QUser
import com.github.kimleepark2.domain.entity.user.dto.response.SellerResponse
import com.github.kimleepark2.domain.entity.wish.QWish.Companion.wish
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@Component
/////// JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
class ProductQueryRepository(
    private val queryFactory: JPAQueryFactory,
) : PaginationSortRepository {

    private val seller = QUser.user

    private val qProductResponse = QProductResponse(
        product.id,
        product.title,
        product.description,
        product.status,
        product.price,
        product.thumbnailImagePath,
        Projections.bean(
            SellerResponse::class.java,
            seller.id,
            seller.username,
            seller.nickname,
            seller.profileImagePath
        ).`as`("seller"),
        wish.countDistinct(),
    )

    private val selectQuery = queryFactory.select(
        qProductResponse
    )
        .from(product)
        .leftJoin(seller).on(
            product.seller.id.eq(seller.id),
            seller.deletedAt.isNull,
        )
        .leftJoin(wish).on(
            product.id.eq(wish.product.id),
            wish.deletedAt.isNull,
        )

    private val countQuery = queryFactory.select(
        product.id.countDistinct(),
    )
        .from(product)
        .leftJoin(seller).on(
            product.seller.id.eq(seller.id),
            seller.deletedAt.isNull,
        )
        .leftJoin(wish).on(
            product.id.eq(wish.product.id),
            wish.deletedAt.isNull,
        )

    fun getById(id: Long): ProductResponse {
        return selectQuery.clone()
            .where(
                product.id.eq(id),
            )
            .fetchOne() ?: throw Exception("상품을 찾을 수 없습니다.")
    }

    fun page(
        sellerName: String?,
        title: String?,
        description: String?,
        status: ProductStatus?,
        minPrice: Int?,
        maxPrice: Int?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable = Pageable.unpaged(),
    ): Page<ProductResponse> {
        val query = selectQuery.clone()
            .where(
                search(
                    sellerName = sellerName,
                    title = title,
                    description = description,
                    status = status,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    pageable = pageable,
                )
            )
        val products: List<ProductResponse> = query
            .distinct()
            .orderBy(
                *pageable.getCustomOrder(
                    customOrderProperties,
                    Product::class.java,
                )
            )
            .pagination(pageable)
            .fetch()

        val count: Long = countQuery.clone()
            .fetchOne() ?: 0

        return PageImpl(products, pageable, count)

    }

    private fun search(
        sellerName: String?,
        title: String?,
        description: String?,
        status: ProductStatus?,
        minPrice: Int?,
        maxPrice: Int?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        pageable: Pageable,
    ): BooleanBuilder {
        val condition = BooleanBuilder()


        condition.and(product.title.startsWith(title))
        condition.and(product.description.contains(description))

        condition.and(product.status.eq(status))

        val minPrice = minPrice
        val maxPrice = maxPrice
        condition.and(product.price.between(minPrice ?: 0, maxPrice ?: 999_999_999))

        if (sellerName != null) {
            condition.and(seller.name.startsWith(sellerName))
        }

        // paged일때만 할 행위 작성
        if (pageable.isPaged) {
            val startTime: LocalDateTime = startDateTime
            val endDate: LocalDateTime = endDateTime

            condition.and(product.createdAt.between(startTime, endDate))
        }


        return condition
    }
}
