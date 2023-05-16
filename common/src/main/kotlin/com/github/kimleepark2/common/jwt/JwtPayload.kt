package com.github.kimleepark2.common.jwt

import io.jsonwebtoken.Claims

class JwtPayload(body: Claims) {
    // 아이디
    private val sub: String

    // 이름
    private val name: String

    // 권한
    private val role: String

    // 작성자
    private val iss: String

    // 생성일
    private val iat: Long

    // 만료일
    private val exp: Long

    init {
        sub = body["sub"].toString()
        name = body["name"].toString()
        role = body["role"].toString()
        iss = body["iss"].toString()
        iat = body["iat"].toString().toLong()
        exp = body["exp"].toString().toLong()
    }
}
