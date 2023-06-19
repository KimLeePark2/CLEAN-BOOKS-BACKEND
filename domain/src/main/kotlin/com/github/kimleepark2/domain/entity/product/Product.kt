package com.github.kimleepark2.domain.entity.product

import com.github.kimleepark2.domain.entity.BaseEntity
import com.github.kimleepark2.domain.entity.file.File
import com.github.kimleepark2.domain.entity.product.enums.ProductStatus
import com.github.kimleepark2.domain.entity.user.User
import com.github.kimleepark2.domain.entity.wish.Wish
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where
import org.hibernate.annotations.WhereJoinTable

@Comment("상품")
@Where(clause = "deleted_at IS NULL")
@Entity
@Table(name = "tb_product")
class Product(
    @Column(name = "title", length = 50, nullable = false)
    @Comment(value = "상품 게시글 제목")
    var title: String,

    @Column(name = "description", length = 8000, nullable = false)
    @Comment(value = "상품 게시글 설명")
    var description: String,

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING) // 문자열로 저장하기 위하여
    @Comment(value = "상태")
    var status: ProductStatus = ProductStatus.SALE,

    @Column(name = "price", length = 9, nullable = false)
    @Comment(value = "책 가격")
    var price: Int,

    // one to many
    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        orphanRemoval = true
    )
    @Comment(value = "상품 이미지들")
    var files: MutableList<File> = mutableListOf(),

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH]
    )
    @JoinColumn(name = "seller_id", foreignKey = ForeignKey(name = "fk_product_seller_id"))
    @WhereJoinTable(clause = "deleted_at IS NULL")
    @Comment(value = "판매자 번호")
    val seller: User,

    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        orphanRemoval = true
    )
    @Comment(value = "상품 찜들")
    var wishes: MutableList<Wish> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "기본키")
    @Column(name = "product_id")
    val id: Long = 0L,
) : BaseEntity() {
    fun update(
        title: String? = null,
        description: String? = null,
        price: Int? = null,
        status: ProductStatus? = null,
    ) {
        this.title = title ?: this.title
        this.description = description ?: this.description
        this.price = price ?: this.price
        this.status = status ?: this.status
    }

    fun addFile(path: String, key: String) {
        files.add(
            File(
                path = path,
                key = key,
                product = this
            )
        )
    }

    fun deleteFile(file: File) {
        files.remove(file)
    }

    fun sale() {
        this.status = ProductStatus.SALE
    }

    fun sold() {
        this.status = ProductStatus.SOLD
    }

    fun wish(user: User) {
        wishes.add(
            Wish(
                product = this,
                user = user,
            )
        )
    }

    fun deleteWish(wish: Wish) {
        wishes.remove(wish)
    }
}