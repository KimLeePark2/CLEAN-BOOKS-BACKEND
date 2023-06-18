package com.github.kimleepark2.common.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*

@Component
class AwsS3Uploader(
    private val amazonS3Client: AmazonS3,
) {

    @Value("\${cloud.aws.s3.bucket}")
    var bucket: String? = null

    @Value("\${cloud.aws.s3.directory-name:static}")
    val S3_BUCKET_DIRECTORY_NAME: String = "static"

    private val log = LoggerFactory.getLogger(AwsS3Uploader::class.java)

    fun upload(multipartFile: MultipartFile, dirName: String): String {
        log.info("S3_BUCKET_DIRECTORY_NAME: $S3_BUCKET_DIRECTORY_NAME")
        // 메타데이터 설정
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = multipartFile.contentType
        objectMetadata.contentLength = multipartFile.size

        // 실제 S3 bucket 디렉토리명 설정
        // 파일명 중복을 방지하기 위한 UUID 추가

        // 경로가 "//"인 경우 replace로 "/"로 변경
        var path = S3_BUCKET_DIRECTORY_NAME + "/$dirName/" + UUID.randomUUID()
        path = path.replace("//", "/")
        val fileName: String = path + "." + multipartFile.originalFilename

        //        removeNewFile(multipartFile)
        return putS3(multipartFile, objectMetadata, fileName)
    }

    // 1. 로컬에 파일생성
    @Throws(IOException::class)
    private fun convert(file: MultipartFile): File {
        // originalFilename 변수에 file.originalFilename을 넣고 file.originalFilename이 빈 문자열이면 temporary로 지정
        val originalFilename: String = if (file.originalFilename.isNullOrBlank()) {
            "temporary"
        } else {
            file.originalFilename!!
        }

        val convertFile = File(originalFilename)

        if (convertFile.createNewFile()) {
            return convertFile
        }

        throw IOException("파일을 서버에 저장하는데 실패했습니다.")
    }

    private fun putS3(uploadFile: MultipartFile, objectMetadata: ObjectMetadata, fileName: String): String =
        try {
            uploadFile.inputStream.use { inputStream ->
                amazonS3Client.putObject(
                    PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                )
            }
            amazonS3Client.getUrl(bucket, fileName).toString()
        } catch (e: IOException) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.message)
            throw IllegalStateException("S3 파일 업로드에 실패했습니다.")
        }

    /**
     * S3에 저장된 파일 삭제
     * @param key S3에 저장된 파일명, 폴더/폴더/.../폴더/파일명.확장자
     */
    fun delete(key: String) {

        val requestKey = listOf(
            S3_BUCKET_DIRECTORY_NAME,
            key,
        ).joinToString("/")
        log.info(
            """
            File Delete
            bucket : $bucket
            S3_BUCKET_DIRECTORY_NAME : $S3_BUCKET_DIRECTORY_NAME
            key : $key
            request key : $requestKey
            """.trimIndent()
        )
        amazonS3Client.deleteObject(bucket, requestKey)
    }

    companion object {
        fun getS3Key(s3BucketFolder: String, filePath: String) = "$s3BucketFolder/${filePath.split("/").last()}"
    }
}