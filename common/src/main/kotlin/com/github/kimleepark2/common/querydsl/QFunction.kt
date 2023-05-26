package com.github.kimleepark2.common.querydsl

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.core.types.dsl.StringTemplate

class QFunction {
    companion object {

        fun distinct(str: StringPath): StringTemplate = Expressions.stringTemplate("distinct {0}", str)

        /**
         * Mariadb의 group_concat 함수를 사용하기 위해 설정
         */
        fun groupConcat(str: StringPath): StringTemplate = Expressions.stringTemplate("group_concat({0})", str)
    }
}