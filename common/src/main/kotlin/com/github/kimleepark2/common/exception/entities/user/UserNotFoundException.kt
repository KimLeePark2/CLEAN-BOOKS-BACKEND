package com.github.kimleepark2.common.exception.entities.user

import com.github.kimleepark2.common.exception.BasicException

class UserNotFoundException(
    override var message: String = "회원 정보를 찾을 수 없습니다."
) : BasicException(404, message)
