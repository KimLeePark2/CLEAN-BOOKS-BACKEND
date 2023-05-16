package com.github.kimleepark2.common.jwt

data class JsonWebTokenDto(
    val grantType: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiresIn: Long? = null,
)
