package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.common.aws.AwsS3Uploader
import com.github.kimleepark2.common.exception.entities.user.UserNotFoundException
import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.github.kimleepark2.domain.entity.user.UserRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productQuery: ProductQueryRepository,
    private val userRepository: UserRepository,
    private val awsS3Uploader: AwsS3Uploader,
) : ProductService {
    override fun createProduct(productCreateRequest: ProductCreateRequest): ProductResponse {
        val user = userRepository.findById(productCreateRequest.userId).orElseThrow { throw UserNotFoundException() }
        var imgPath = ""

        if (productCreateRequest.thumbnailImage != null) {
            imgPath = awsS3Uploader.upload(productCreateRequest.thumbnailImage!!, "product")
        }

        val product = productCreateRequest.toEntity(imgPath, user)
        productRepository.save(product)
        return ProductResponse(product)
    }

    override fun getProductById(id: Long): ProductResponse {
        TODO("Not yet implemented")
    }

    override fun updateProduct(userUpdateRequest: ProductUpdateRequest) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(id: Long): ProductResponse {
        TODO("Not yet implemented")
    }
}