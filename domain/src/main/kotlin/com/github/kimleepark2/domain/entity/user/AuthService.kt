package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.request.LoginRequest
import com.github.kimleepark2.domain.entity.user.dto.request.RefreshTokenRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserUpdateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import com.github.kimleepark2.domain.entity.user.enum.OAuth2Provider

interface AuthService {
    fun login(provider: OAuth2Provider, providerId: String): LoginResponse
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken
    fun kakaoLogout(): String
}