package com.github.kimleepark2.common.exception.auth

import com.github.kimleepark2.common.exception.UnauthorizedException

class OnlyLaundryException : UnauthorizedException("세탁소 관리자만 가능합니다.")