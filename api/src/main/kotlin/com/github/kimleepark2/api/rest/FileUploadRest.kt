package com.github.kimleepark2.api.rest

import com.github.kimleepark2.common.aws.AwsS3Uploader
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
@Tag(name = "999. S3 테스트", description = "S3파일 테스트용")
class FileUploadRest(
    private val awsS3Uploader: AwsS3Uploader,
) {

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "S3 파일 업로드(테스트 기능)",
        description = "단일 파일 업로드 테스트",

    )
    @PostMapping("", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun uploadFile(
        @Parameter(name = "file", description = "Multipartfile") @RequestParam("file") file: MultipartFile,
    ): String {
        return awsS3Uploader.upload(file, "test")
    }
}