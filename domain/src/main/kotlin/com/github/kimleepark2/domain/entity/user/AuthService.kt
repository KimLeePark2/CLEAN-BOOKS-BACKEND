package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.request.RefreshTokenRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider

interface AuthService {
    fun login(provider: OAuth2Provider, providerId: String): LoginResponse
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken
    fun kakaoLogout(): String
}