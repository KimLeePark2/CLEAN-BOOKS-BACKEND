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

    override fun save(productCreateRequest: ProductCreateRequest): Long {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val user = userCommand.findById(productCreateRequest.userId).orElseThrow { throw UserNotFoundException() }
        val product: Product = productCreateRequest.toEntity(user)

        val thumbnailImages = productCreateRequest.thumbnailImages?.subList(0, maxThumbnailSize)
        log.info("thumbnailImages.size : ${thumbnailImages?.size ?: 0}")
        thumbnailImages?.forEach { file ->
            val filePath = awsS3Uploader.upload(file, "product")
            product.addFile(filePath)
        }
        product.create(loginUser.username)
        productCommand.save(product)
        return product.id
    }

    override fun update(id: Long, productUpdateRequest: ProductUpdateRequest): Long {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val sellerId = loginUser.id
        val product: Product = productCommand.findByIdAndSellerId(id, sellerId) ?: throw ProductNotFoundException()

        // 삭제할 파일 경로들을 받아서, 파일을 삭제하고, product에서도 삭제
        productUpdateRequest.deleteFilePaths?.forEach {existFilePath ->
            log.info("existFilePath : $existFilePath")
            fileCommand.findByPath(existFilePath)?.let {
                log.info("exist file: $it")
                awsS3Uploader.delete(it.path)
                product.deleteFile(it)
            }
        }

        // 새로 등록할 파일을 받아서 업로드하고, product에 추가
        val thumbnailImages = productUpdateRequest.thumbnailImages?.subList(0, maxThumbnailSize)
        log.info("thumbnailImages.size : ${thumbnailImages?.size ?: 0}")
        productUpdateRequest.thumbnailImages?.forEach { newFile->
            log.info("file: $newFile")
            val newPath = awsS3Uploader.upload(newFile, "product")
            product.addFile(newPath)
        }

        product.update(
            title = productUpdateRequest.title,
            description = productUpdateRequest.description,
            price = productUpdateRequest.price,
        )

        product.update(loginUser.username)
        productCommand.save(product)
        return product.id
    }

    override fun deleteById(id: Long) {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val sellerId = loginUser.id
        val product: Product = productCommand.findByIdAndSellerId(id, sellerId) ?: throw ProductNotFoundException()
        product.softDelete(loginUser.username)
        product.files
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