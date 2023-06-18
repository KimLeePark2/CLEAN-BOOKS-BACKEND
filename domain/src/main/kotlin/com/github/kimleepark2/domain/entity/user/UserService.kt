package com.github.kimleepark2.domain.entity.user

import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.request.UserUpdateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse

interface UserService : AuthService {
    fun saveUser(userCreateRequest: UserCreateRequest): LoginResponse
    fun updateUser(userUpdateRequest: UserUpdateRequest)
    fun deleteUser(id: String): UserResponse
    fun getUserById(id: String): UserResponse
    fun getUserByUsername(username: String): UserResponse
    fun list(): List<UserResponse>
}