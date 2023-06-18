package com.github.kimleepark2.api.config

import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import jakarta.persistence.EntityManager

@Configuration
class QuerydslConfig(
    private val em: EntityManager,
) {
//    @Bean
//    fun querydsl(): JPAQueryFactory {
//        return JPAQueryFactory(em)
//    }
    @Bean
    fun queryDsl(): JPAQueryFactory {
        // transform 사용 시 에러발생 해결
        return JPAQueryFactory(JPQLTemplates.DEFAULT, em)
    }
}
