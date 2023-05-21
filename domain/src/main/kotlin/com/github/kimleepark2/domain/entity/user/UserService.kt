package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.common.jwt.dto.JwtToken
import com.github.kimleepark2.domain.entity.user.dto.request.RefreshTokenRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserUpdateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse

interface UserService {
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken

    fun getUserById(id: Long): User

    fun getLoginUserInfo(): User

    fun getUserByUsername(username: String): User

    fun updateUser(userUpdateRequest: UserUpdateRequest)

    fun deleteUser(id: Long): UserResponse

    fun kakaoLogout(): String
}