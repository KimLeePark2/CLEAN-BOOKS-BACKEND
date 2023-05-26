package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.common.aws.AwsS3Uploader
import com.github.kimleepark2.common.exception.entities.product.ProductNotFoundException
import com.github.kimleepark2.common.exception.entities.user.UserNotFoundException
import com.github.kimleepark2.domain.entity.product.dto.request.ProductCreateRequest
import com.github.kimleepark2.domain.entity.product.dto.request.ProductUpdateRequest
import com.github.kimleepark2.domain.entity.product.dto.response.ProductResponse
import com.github.kimleepark2.domain.entity.user.UserRepository
import com.github.kimleepark2.domain.entity.user.UserServiceImpl
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productQuery: ProductQueryRepository,
    private val userRepository: UserRepository,
    private val awsS3Uploader: AwsS3Uploader,
) : ProductService {
    override fun createProduct(productCreateRequest: ProductCreateRequest): ProductResponse {
        val loginUser = UserServiceImpl.getAccountFromSecurityContext()
        val user = userRepository.findById(productCreateRequest.userId).orElseThrow { throw UserNotFoundException() }
        var imgPath = ""

        if (productCreateRequest.thumbnailImage != null) {
            imgPath = awsS3Uploader.upload(productCreateRequest.thumbnailImage!!, "product")
        }

        val product = productCreateRequest.toEntity(imgPath, user)
        product.create(loginUser.username)
        productRepository.save(product)
        return ProductResponse(product)
    }

    override fun getById(id: Long): ProductResponse {
//        val product = productRepository.findById(id).orElseThrow { throw ProductNotFoundException("상품을 찾을 수 없습니다.") }
        return productQuery.getById(id)
    }

    override fun update(userUpdateRequest: ProductUpdateRequest) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long): ProductResponse {
        TODO("Not yet implemented")
    }
}