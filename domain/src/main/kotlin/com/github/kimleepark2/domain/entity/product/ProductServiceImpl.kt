package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.common.aws.AwsS3Uploader
import com.github.kimleepark2.common.exception.entities.product.ProductNotFoundException
import com.github.kimleepark2.common.exception.entities.user.UserNotFoundException
import com.github.kimleepark2.domain.entity.file.FileRepository
import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductPageRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.github.kimleepark2.domain.entity.user.UserRepository
import com.github.kimleepark2.domain.entity.user.UserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ProductServiceImpl(
    private val productCommand: ProductRepository,
    private val productQuery: ProductQueryRepository,
    private val userCommand: UserRepository,
    private val fileCommand: FileRepository,
    private val awsS3Uploader: AwsS3Uploader,
) : ProductService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${product.thumbnail.max-size}")
    private val maxThumbnailSize: Int = 0

    private final val S3_BUCKET_FOLDER = "product"

    @Transactional
    override fun save(productCreateRequest: ProductCreateRequest): Long {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val user = userCommand.findById(productCreateRequest.userId).orElseThrow { throw UserNotFoundException() }
        val product: Product = productCreateRequest.toEntity(user)

        productCreateRequest.thumbnailImages?.let { files ->
            val maxSize = getFilesMaxSize(files)
            log.info("file add max size : $maxSize")
            files.subList(0, maxSize).forEach { file ->
                addFile(file, product)
            }
        }

        product.create(loginUser.username)
        productCommand.save(product)
        return product.id
    }

    private fun getFilesMaxSize(files: List<MultipartFile>) =
        if (files.size > maxThumbnailSize) maxThumbnailSize else files.size

    private fun addFile(
        file: MultipartFile,
        product: Product
    ) {
        val filePath = awsS3Uploader.upload(file, S3_BUCKET_FOLDER)
        val key = AwsS3Uploader.getS3Key(S3_BUCKET_FOLDER, filePath)
        log.info("s3 key : $key")
        product.addFile(filePath, key)
        productCommand.save(product)
    }

    override fun update(id: Long, productUpdateRequest: ProductUpdateRequest): Long {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val sellerId = loginUser.id
        val product: Product = productCommand.findByIdAndSellerId(id, sellerId) ?: throw ProductNotFoundException()

        // 삭제할 파일 경로들을 받아서, 파일을 삭제하고, product에서도 삭제
        productUpdateRequest.deleteFilePaths?.forEach { existFilePath ->
            log.info("existFilePath : $existFilePath")
            fileCommand.findByPathAndProduct(existFilePath, product)?.let {
                log.info("exist file: $it")
                awsS3Uploader.delete(it.key)
                product.deleteFile(it)
            }
        }

        product.update(
            title = productUpdateRequest.title,
            description = productUpdateRequest.description,
            price = productUpdateRequest.price,
        )

        product.update(loginUser.username)
        productCommand.save(product)

        // 새로 등록할 파일을 받아서 업로드하고, product에 추가
        productUpdateRequest.thumbnailImages?.let { files ->
            val maxSize = getFilesMaxSize(files)
            log.info("file add max size : $maxSize")
            files.subList(0, maxSize).forEach { file ->
                addFile(file, product)
            }
        }
        return product.id
    }

    override fun deleteById(id: Long) {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val sellerId = loginUser.id
        val product: Product = productCommand.findByIdAndSellerId(id, sellerId) ?: throw ProductNotFoundException()
        product.softDelete(loginUser.username)
        product.files.forEach { file ->
            awsS3Uploader.delete(file.key)
        }
        productCommand.save(product)
    }

    override fun getById(id: Long): ProductResponse {
        val product = productCommand.findById(id).orElseThrow { throw ProductNotFoundException("상품을 찾을 수 없습니다.") }
        return ProductResponse(product)
//        return productQuery.getById(id)
    }

    override fun page(productPageRequest: ProductPageRequest, pageable: Pageable): Page<ProductResponse> {
        return productQuery.page(
            condition = productPageRequest,
            pageable = pageable,
        )
    }
}