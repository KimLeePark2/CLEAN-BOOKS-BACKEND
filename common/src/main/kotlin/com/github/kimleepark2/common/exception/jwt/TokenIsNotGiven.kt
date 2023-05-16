package com.github.kimleepark2.common.exception.jwt

import com.github.kimleepark2.common.exception.BasicException

class TokenIsNotGiven(name: String) : BasicException(400, name + "토큰이 넘어오지 않았습니다.")
