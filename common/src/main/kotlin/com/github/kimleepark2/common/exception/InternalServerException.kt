package com.github.kimleepark2.common.exception

class InternalServerException : BasicException {
    @JvmOverloads
    constructor(message: String = "서버 에러 발생") : super(500, message) {
    }
}
