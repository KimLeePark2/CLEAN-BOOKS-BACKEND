package com.github.kimleepark2.common.paging

import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@ParameterObject
class PageRequest {
    @Schema(description = "페이지 번호(1부터 시작)", defaultValue = "1")
    var page: Int = 1

    @Schema(description = "페이지 크기", defaultValue = "10")
    var size: Int = 10

    @Schema(description = "정렬 정보", defaultValue = "id.desc")
    var sort: List<String>? = null

    @Schema(description = "페이지네이션 사용 여부/false로 주면 모든 데이터를 가져옴", defaultValue = "true")
    var paged = true

    fun setPage(page: Int): PageRequest {
        this.page = if (page <= 0) 1 else page
        return this
    }

    fun setSize(size: Int): PageRequest {
        val DEFAULT_SIZE = 10
        val MAX_SIZE = Int.MAX_VALUE
        this.size = if (size > MAX_SIZE) DEFAULT_SIZE else size
        return this
    }

    fun of(): Pageable {
        // 사용자 정의 sort가 오지 않았을때 id desc를 기본으로 사용하기 위해 생성.
        val defaultOrder = Sort.Order.desc("id")

        val orders: List<Sort.Order> = sort?.filter {
            // name.asc 같은 형태로 와야 정렬처리 하므로 필터링한다.

            // 외래 테이블 조인해야하는 경우 table.column.asc 형태로 오기 때문에 2이상으로 필터링한다.
            it.split(".").size >= 2
        }?.map {
            // .으로 필터링된 정렬 값을 분리시킨다.
            val split = it.split(".")
            // properties의 정렬 방향을 설정한다.

            // split의 0부터 n-1까지
            val lastValueIndex = split.size - 1

            val properties = split.subList(0, lastValueIndex).joinToString(".")

            // split의 마지막 값은 항상 split.size - 1임.
            val directionValue = split[lastValueIndex].equals("asc", true)

            // direction은 asc로 왔을때만 asc로 설정한다.
            val direction = if (directionValue) Sort.Direction.ASC else Sort.Direction.DESC
            Sort.Order(direction, properties)
        } ?: listOf(defaultOrder)

        // 위에서 계산한 값으로 정렬값 생성
        val sortBy = Sort.by(orders)

        return if (paged) {
            org.springframework.data.domain.PageRequest.of(page - 1, size, sortBy)
        } else {
            Pageable.unpaged()
        }
    }

    override fun toString(): String {
        return "PageRequest(page=$page, size=$size, sort=$sort, paged=$paged)"
//        return "PageRequest(page=$page, size=$size, direction=$direction, sort=$sort, paged=$paged)"
    }
}