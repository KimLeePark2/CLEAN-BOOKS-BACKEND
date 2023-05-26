package com.github.kimleepark2.domain.entity.product

//@Component
// JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
//class ProductQueryRepository(
//    private val queryFactory: JPAQueryFactory,
//) {
//
//    fun getById(id: Long): ProductResponse {
//        return queryFactory.select<ProductResponse>(
//            QProductResponse(
//                product.id,
//                product.title,
//                product.status,
//                product.bookTitle,
//                product.author,
//                product.price,
//                product.publisher,
//                product.thumbnailImagePath,
//                Projections.bean(
//                    SellerResponse::class.java,
//                    seller.id,
//                    seller.username,
//                    seller.nickname,
//                    seller.profileImagePath
//                ).`as`("seller"),
//                wish.countDistinct(),
//            )
//        )
//            .from(product)
//            .leftJoin(seller).on(
//                product.seller.id.eq(seller.id),
//                seller.deletedAt.isNull,
//            )
//            .leftJoin(wish).on(
//                product.id.eq(wish.product.id),
//                wish.deletedAt.isNull,
//            )
//            .where(
//                product.id.eq(id),
//                product.deletedAt.isNull,
//            )
//            .fetchOne() ?: throw Exception("상품을 찾을 수 없습니다.")
//    }
//}

/*

    .transform(
        groupBy(itemEntity.id)
        .list(
            Projections.bean(
                Dto.class,
                itemEntity.필드,
                itemEntity.필드,
                list(
                    Projections.bean(
                        자식Dto.class,
                        자식Entity.필드,
                        자식Entity.필드
    )));


 */