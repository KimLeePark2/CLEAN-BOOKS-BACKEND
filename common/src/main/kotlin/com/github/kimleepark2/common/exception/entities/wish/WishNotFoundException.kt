package com.github.kimleepark2.common.exception.entities.wish

import com.github.kimleepark2.common.exception.BasicException

class WishNotFoundException(
    msg: String = "찜 정보를 찾을 수 없습니다."
) : BasicException(400, msg)
