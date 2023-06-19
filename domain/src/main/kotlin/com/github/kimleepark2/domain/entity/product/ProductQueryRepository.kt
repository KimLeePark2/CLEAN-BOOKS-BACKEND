package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.common.paging.PaginationSortRepository
import com.github.kimleepark2.domain.entity.file.QFile.Companion.file
import com.github.kimleepark2.domain.entity.product.QProduct.Companion.product
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.github.kimleepark2.domain.entity.product.dto.response.QProductResponse
import com.github.kimleepark2.domain.entity.user.QUser
import com.github.kimleepark2.domain.entity.user.dto.response.SellerResponse
import com.github.kimleepark2.domain.entity.wish.QWish.Companion.wish
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
// ///// JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
class ProductQueryRepository(
    private val queryFactory: JPAQueryFactory,
) : PaginationSortRepository {

    override val customOrderProperties: Map<String, String> = mapOf()

    private val seller = QUser.user

    private val qProductResponse = QProductResponse(
        product.id,
        product.title,
        product.description,
        product.status,
        product.price,
        list(file.path),
        Projections.constructor(
            SellerResponse::class.java,
            seller.id,
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
        .leftJoin(product.files, file)

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

    fun getById(id: Long): ProductResponse? {
        return null
//        val selectQuery1 = selectQuery
//        return selectQuery
//            .where(
//                product.id.eq(id),
//            )
//            .fetchOne() ?: throw Exception("상품을 찾을 수 없습니다.")
    }

    fun page(
        condition: ProductPageRequest,
        pageable: Pageable = Pageable.unpaged(),
    ): Page<ProductResponse> {
        val products: List<ProductResponse> = queryFactory
            .selectFrom(product)
            .leftJoin(seller).on(
                product.seller.id.eq(seller.id),
                seller.deletedAt.isNull,
            ).fetchJoin()
            .leftJoin(wish).on(
                product.id.eq(wish.product.id),
                wish.deletedAt.isNull,
            ).fetchJoin()
            .leftJoin(product.files, file).fetchJoin()
            .where(
                search(
                    condition = condition,
                    pageable = pageable,
                )
            )
            .orderBy(
                *pageable.getCustomOrder(
                    customOrderProperties,
                    Product::class.java,
                )
            )
            .pagination(pageable)
            .fetch().map {
                ProductResponse(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    status = it.status,
                    price = it.price,
                    thumbnailImagePaths = it.files.map { file -> file.path },
                    seller = SellerResponse(it.seller),
                    wishes = it.wishes.size.toLong(),
                )
            }

//        val products = queryFactory
//            .selectFrom(product)
//            .leftJoin(seller).on(
//                product.seller.id.eq(seller.id),
//                seller.deletedAt.isNull,
//            )
//            .leftJoin(wish).on(
//                product.id.eq(wish.product.id),
//                wish.deletedAt.isNull,
//            )
//            .leftJoin(file).on(
//                product.id.eq(file.product.id),
//                file.deletedAt.isNull,
//            )
//            .distinct()
//            .transform(
//                groupBy(product.id).list(
//                    Projections.constructor(
//                        ProductResponse::class.java,
//                        product.id.`as`("id"),
//                        product.title.`as`("title"),
//                        product.description.`as`("description"),
//                        product.status.`as`("status"),
//                        product.price.`as`("price"),
// //                        Expressions.asString(""),
//                        list(file.path).`as`("thumbnailImagePaths"),
//                        Projections.constructor(
//                            SellerResponse::class.java,
//                            seller.id,
//                            seller.username,
//                            seller.nickname,
//                            seller.profileImagePath
//                        ).`as`("seller"),
//                        wish.countDistinct().`as`("wishes"),
//                    )
//                )
//            )
//        val query = selectQuery.clone()
//            .where(
//                search(
//                    condition = condition,
//                    pageable = pageable,
//                )
//            )
//        val products: List<ProductResponse> = query
//            .distinct()
//            .orderBy(
//                *pageable.getCustomOrder(
//                    customOrderProperties,
//                    Product::class.java,
//                )
//            )
//            .pagination(pageable)
//            .fetch()
//
        val count: Long = countQuery.clone()
            .fetchOne() ?: 0

        return PageImpl(products, pageable, count)
    }

    private fun search(
        condition: ProductPageRequest,
        pageable: Pageable,
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        condition.title?.let { builder.and(product.title.startsWith(it)) }
        condition.description?.let { builder.and(product.description.contains(it)) }
        condition.status?.let { builder.and(product.status.eq(it)) }
        condition.isWish?.let { builder.and(wish.id.isNotNull) }
        condition.isMine?.let { builder.and(product.seller.id.eq(condition.sellerId)) }
//        val minPrice: Long = condition.minPrice?.toLong() ?: 0
//        val maxPrice: Long = condition.maxPrice?.toLong() ?: Long.MAX_VALUE
        // between 으로 쓰니 int타입 불가능하다고 함... 왜인지 모르겠음
        // https://github.com/querydsl/querydsl/pull/3346 querydsl에 issue가 올라가있음. 현재 사용 불가?
//        builder.and(
//            product
//                .price
//                .goe(
//                    minPrice
//                )
//        )
//        builder.and(
//            product.price.loe(
//                maxPrice
//            )
//        )
        condition.sellerName?.let { builder.and(seller.name.startsWith(it)) }

        // 날짜 검색이 존재할 경우 사용
        if (condition.isDateRangeValid) {
            val startTime: LocalDateTime = condition.startDateTime
            val endDate: LocalDateTime = condition.endDateTime
            builder.and(product.createdAt.between(startTime, endDate))
        }

        return builder
    }
}
