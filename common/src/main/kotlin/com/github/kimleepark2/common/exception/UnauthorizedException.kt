package com.github.kimleepark2.common.exception

open class UnauthorizedException : BasicException {
    constructor() : super(401, "계정 정보 인증에 실패했습니다.") {}
    constructor(message: String) : super(401, message) {}
}
