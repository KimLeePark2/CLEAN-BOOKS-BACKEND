package com.github.kimleepark2.api.rest

import com.github.kimleepark2.domain.entity.user.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "000. 사용자", description = "사용자 관리 API")
class UserRest(
    private val userService: UserService,
) {

//    @ApiResponses(
//        ApiResponse(responseCode = "200", description = "성공"),
//        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
//    )
//    @Operation(
//        summary = "사용자 등록",
//        description = "OAuth2 사용자 정보로 등록"
//    )
//    @PostMapping("")
//       @ResponseBody
//       @ResponseStatus(HttpStatus.CREATED)
//    fun createUser(@RequestBody userCreateRequest: UserCreateRequest): UserResponse{
//
//    }
}