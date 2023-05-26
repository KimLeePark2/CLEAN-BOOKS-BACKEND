package com.github.kimleepark2.common.exception.entities.product

import com.github.kimleepark2.common.exception.BasicException

class ProductNotFoundException(
    msg: String = "데이터를 찾을 수 없습니다."
) : BasicException(400, msg)
