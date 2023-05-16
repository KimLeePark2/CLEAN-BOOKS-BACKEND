package com.github.kimleepark2.common.exception

class MyEntityNotFoundException : BasicException {
    constructor() : super(400, "데이터를 찾을 수 없습니다.") {}
    constructor(msg: String) : super(400, msg) {}
}
