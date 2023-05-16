package com.github.kimleepark2.common.exception.auth

import com.github.kimleepark2.common.exception.UnauthorizedException

class OnlyClientException : UnauthorizedException("거래처 관리자만 가능합니다.")