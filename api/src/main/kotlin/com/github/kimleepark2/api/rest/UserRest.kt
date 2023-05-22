package com.github.kimleepark2.api.rest

import com.amazonaws.services.s3.model.ObjectMetadata
import com.github.kimleepark2.domain.entity.user.UserService
import com.github.kimleepark2.domain.entity.user.dto.request.UserCreateRequest
import com.github.kimleepark2.domain.entity.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "000. 사용", description = "사용자 관리 API")
class UserRest(
    private val userService: UserService,
) {

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "사용자 등록",
        description = "OAuth2 사용자 정보로 등록"
    )
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userCreateRequest: UserCreateRequest): UserResponse{

    }
}