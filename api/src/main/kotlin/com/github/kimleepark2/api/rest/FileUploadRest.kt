package com.github.kimleepark2.api.rest

import com.github.kimleepark2.common.aws.AwsS3Uploader
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
@Tag(name = "999. S3 테스트", description = "S3파일 테스트용")
class FileUploadRest(
    private val awsS3Uploader: AwsS3Uploader,
) {
    private val log = LoggerFactory.getLogger(FileUploadRest::class.java)

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청 정보"),
    )
    @Operation(
        summary = "S3 파일 업로드(테스트 기능)",
        description = "단일 파일 업로드 테스트",
    )
    @PostMapping("", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
    ): FileResponse {
//    : ResponseEntity<String> {
//        val path = awsS3Uploader.upload(file, "test").trimIndent().trim()
//        log.info("path: $path")
//        return ResponseEntity.ok().body(path)
        return FileResponse(awsS3Uploader.upload(file, "test").trimIndent().trim())
    }

    inner class FileResponse(
        val path: String,
    )
}