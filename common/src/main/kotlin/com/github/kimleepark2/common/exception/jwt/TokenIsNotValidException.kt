package com.github.kimleepark2.common.exception.jwt

import com.github.kimleepark2.common.exception.BasicException

class TokenIsNotValidException : BasicException(400, "토큰정보가 비정상입니다.")
