package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.request.RefreshTokenRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserUpdateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse

interface UserService {
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken

    fun getUserById(id: String): UserResponse

    fun getUserByUsername(username: String): UserResponse

    fun updateUser(userUpdateRequest: UserUpdateRequest)

    fun deleteUser(id: String): UserResponse

    fun kakaoLogout(): String

    fun testLogin(): LoginResponse
    fun saveUser(userCreateRequest: UserCreateRequest): User
}