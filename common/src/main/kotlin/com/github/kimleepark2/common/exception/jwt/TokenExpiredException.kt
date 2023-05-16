package com.github.kimleepark2.common.exception.jwt

import com.github.kimleepark2.common.exception.BasicException

class TokenExpiredException : BasicException(401, "로그인 세션이 만료되었습니다.")
