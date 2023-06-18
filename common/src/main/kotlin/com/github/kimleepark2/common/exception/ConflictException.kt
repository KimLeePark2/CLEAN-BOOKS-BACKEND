package com.github.kimleepark2.common.exception

class ConflictException @JvmOverloads constructor(
    message: String = "중복된 데이터입니다."
) : BasicException(403, message)
