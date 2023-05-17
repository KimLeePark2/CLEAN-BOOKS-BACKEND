package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.domain.entity.BaseEntity
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import com.github.kimleepark2.domain.entity.wish.Wish
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where

@Comment("상품")
@Where(clause = "deleted_at IS NULL")
@Entity
@Table(name = "tb_product")
class Product(

    @Column(name = "title", length = 20, nullable = false)
    @Comment(value = "상품 게시글 제목")
    var title: String,

    @Column(name = "thumbnail_image_path", length = 20, nullable = false)
    @Comment(value = "사용자 프로필 경로(S3 Path)")
    var thumbnailImagePath: String,

    @Column(name = "price", length = 20, nullable = false)
    @Comment(value = "상품 가격")
    var price: Integer,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH ], orphanRemoval = true)
    @Comment(value = "상품 찜들")
    var wishes: MutableList<Wish>,

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING) // 문자열로 저장하기 위하여
    @Comment(value = "상태")
    var status: ProductStatus = ProductStatus.SALE,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "기본키")
    @Column(name = "product_id")
    val id: Long? = null,
) : BaseEntity() {
}