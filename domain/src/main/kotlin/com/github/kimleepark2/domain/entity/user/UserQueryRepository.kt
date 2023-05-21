package com.github.kimleepark2.domain.entity.user

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
// JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
class UserQueryRepository(
    private val queryFactory: JPAQueryFactory,
)