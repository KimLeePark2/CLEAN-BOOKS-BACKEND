package com.github.kimleepark2.common.exception

class BadRequestException : BasicException {
    constructor() : super(400, "잘못된 요청입니다. 데이터를 확인해주세요.") {}
    constructor(message: String) : super(400, message) {}
}
