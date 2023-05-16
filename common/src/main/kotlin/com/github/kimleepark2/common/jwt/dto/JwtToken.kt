package com.github.kimleepark2.common.jwt.dto

open class JwtToken(
    open val refreshToken: String,
    open val accessToken: String,
    val tokenType: String = "Bearer ",
)
