package com.github.kimleepark2.common.paging

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.dsl.StringPath
import kr.co.jsol.core.util.pascalToCamel
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort


interface PaginationSortRepository : PaginationRepository {

    val customOrderProperties: Map<String, String>

    /**
     * 페이지네이션으로 받은 Sort를 가지고 정렬을 한다.
     * @param customOrderProperties front에서 받을 값 to 실제로 db property 값으로 사용할 값을 입력한다.
     * @param entityClass 커스텀 정렬을 사용하지 않을 경우 기본으로 정렬에 사용할 from 절에 사용한 db entity class를 입력한다.
     * @param entityAlias 기본 값은 Entity ClassName을 Pascal to CamelCase한 값인데 다를 수도 있으므로, 클래스와 실제로 조회할 DB 값이 다르다면 입력한다.
     *
     * @return OrderSpecifier 배열을 반환한다. .order에서는 각각 단일 정렬 값을 받기 때문에 *getCustomOrder 과 같이 배열을 풀어주면 된다.
     */
    fun Pageable.getCustomOrder(
        customOrderProperties: Map<String, String>,
        entityClass: Class<*>,
        entityAlias: String = entityClass.simpleName.pascalToCamel(),
    ): Array<OrderSpecifier<*>> {
        val sort = this.sort
        var orders: Array<OrderSpecifier<*>> = arrayOf()

        // 커스텀 정렬 기능, 커스텀 정렬 대상이 없으면 기본 클래스를 대상으로 정렬한다.
        if (sort.isSorted && customOrderProperties.isNotEmpty()) {
            sort.stream().findFirst().ifPresent { order: Sort.Order ->
                val direction = if (order.isAscending) Order.ASC else Order.DESC
                val property = order.property
                val orderByExpression: StringPath = Expressions.stringPath(property)

                if (customOrderProperties.contains(property)) {
                    try {
                        orders =
                            orders.plus(
                                OrderSpecifier(
                                    direction,
                                    // customOrderProperties에 정의된 property가 있으면 해당 property를 사용하고 없으면 기존의 property를 사용한다.
                                    Expressions.stringPath(customOrderProperties[property]) ?: orderByExpression
                                )
                            )
                    } catch (_: Exception) {
                    }
                }
            }
        }

        // 커스텀 정렬된 값이 없으면 기본으로 지정된 entityClass sort를 사용한다.
        if (orders.isEmpty()) {
            orders = getOrderSpecifiers(sort, entityClass, entityAlias)
        }

        return orders
    }

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