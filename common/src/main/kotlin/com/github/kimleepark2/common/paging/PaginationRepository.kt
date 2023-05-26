package com.github.kimleepark2.common.paging

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable

interface PaginationRepository {
    val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun <T> JPAQuery<T>.pagination(pageable: Pageable): JPAQuery<T> {
        return this.offset(getOffset(pageable))
            .limit(getLimit(pageable))
    }

    private fun getOffset(pageable: Pageable): Long {
        return if (pageable.isPaged) {
            pageable.offset
        } else {
            0
        }
    }

    private fun getLimit(pageable: Pageable): Long {
        return if (pageable.isPaged) {
            pageable.pageSize.toLong()
        } else {
            Long.MAX_VALUE
        }
    }

    fun isNamesContainsIn(column: StringPath, names: List<String>?): BooleanExpression? {
        if (names.isNullOrEmpty()) return null
        return Expressions.booleanTemplate(
            "function('regexp', {0}, {1}) = true",
            column, // ex QClient.client.name
            names.joinToString("|")
        )
    }
}