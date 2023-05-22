package com.github.kimleepark2.common.aws

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import java.util.Optional
import java.util.UUID

@Component
class AwsS3Uploader(
    private val amazonS3Client: AmazonS3Client,
) {

    @Value("\${cloud.aws.s3.bucket}")
    var bucket: String? = null

    private val log = LoggerFactory.getLogger(AwsS3Uploader::class.java)

    fun upload(multipartFile: MultipartFile, dirName: String): String {
        val uploadFile = convert(multipartFile) // 파일 생성
        return upload(uploadFile, dirName)
    }

    private fun upload(uploadFile: File, dirName: String): String {
        val fileName = dirName + "/" + UUID.randomUUID() + uploadFile.name
        val uploadImageUrl = putS3(uploadFile, fileName) // s3로 업로드
        removeNewFile(uploadFile)
        return uploadImageUrl
    }

    // 1. 로컬에 파일생성
    @Throws(IOException::class)
    private fun convert(file: MultipartFile): File {
        // originalFilename 변수에 file.originalFilename을 넣고 file.originalFilename이 빈 문자열이면 temporary로 지정
        val originalFilename = if (file.originalFilename.isNullOrBlank()) {
            "temporary"
        } else {
            file.originalFilename
        }

        return File(
            Objects.requireNonNull(originalFilename)
        )
    }

    // 2. S3에 파일업로드
    private fun putS3(uploadFile: File, fileName: String): String {
        amazonS3Client.putObject(
            PutObjectRequest(
                bucket,
                fileName,
                uploadFile
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )
        log.info("File Upload : $fileName");
        return amazonS3Client.getUrl(bucket, fileName).toString()
    }

    // 3. 로컬에 생성된 파일삭제
    private fun removeNewFile(targetFile: File) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return
        }
        log.info("File delete fail");
    }

    fun delete(fileName: String?) {
        log.info("File Delete : $fileName");
        amazonS3Client.deleteObject(bucket, fileName)
    }
}