package com.github.kimleepark2.common.exception.auth

import com.github.kimleepark2.common.exception.UnauthorizedException

class OnlyJsolException(msg: String? = null) : UnauthorizedException(msg ?: "제이솔루션 관리자만 가능합니다.")