package com.github.kimleepark2.api.rest

import com.github.kimleepark2.domain.entity.user.UserService
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.LoginResponse
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "100. 사용자", description = "사용자 관리 API")
class UserRest(
    private val userService: UserService,
) {
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @Operation(
        summary = "모든 사용자 정보 조회",
        description = "모든 사용자 정보 조회"
    )
    @GetMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun getMyInfo(): List<UserResponse> {
        return userService.list()
    }
}