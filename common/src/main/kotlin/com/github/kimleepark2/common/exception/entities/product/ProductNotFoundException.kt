package com.github.kimleepark2.common.exception.entities.product

import com.github.kimleepark2.common.exception.BasicException

class ProductNotFoundException(
    msg: String = "상품을 찾을 수 없습니다."
) : BasicException(400, msg)
