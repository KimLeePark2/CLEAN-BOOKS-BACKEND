package com.github.kimleepark2.common.paging

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Sort

interface PaginationSortRepository : PaginationRepository {
    fun getOrderSpecifiers(sort: Sort, classType: Class<*>, className: String?): Array<OrderSpecifier<*>> {
        val orders: MutableList<OrderSpecifier<*>> = ArrayList()
        // Sort
        sort.stream().forEach { order: Sort.Order ->
            val direction = if (order.isAscending) Order.ASC else Order.DESC
            val orderByExpression: PathBuilder<*> = PathBuilder(classType, className)
            orders.add(OrderSpecifier(direction, orderByExpression[order.property] as Expression<Comparable<*>>))
        }
        return orders.toTypedArray()
    }
}