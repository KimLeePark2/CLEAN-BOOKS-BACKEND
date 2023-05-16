package com.github.kimleepark2.common.paging

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable

interface PaginationRepository {

    val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun getOffset(pageable: Pageable?): Long {
        return if (pageable != null && pageable.isPaged) {
            pageable.offset
        } else {
            0
        }
    }

    fun getLimit(pageable: Pageable?): Long {
        return if (pageable != null && pageable.isPaged) {
            pageable.pageSize.toLong()
        } else {
            Long.MAX_VALUE
        }
    }

    fun isNamesContainsIn(column: StringPath, names: List<String>?): BooleanExpression? {
        // 아래와 같은 쿼리를 만드려고 했으나 regexp가 unexpected token 오류 발생...
        // client는 데이터가 1천개 이상 갈 일이 없어보이므로 like '%name%'으로 대체

//                select c.name
//                from tb_client c
//                 where c.name regexp '거|래';
        log.info("column: $column, names: $names")

        if (names.isNullOrEmpty()) return null

        return Expressions.booleanTemplate(
            "function('regexp', {0}, {1}) = true",
            column, // ex QClient.client.name
            names.joinToString("|")
        )
    }
}