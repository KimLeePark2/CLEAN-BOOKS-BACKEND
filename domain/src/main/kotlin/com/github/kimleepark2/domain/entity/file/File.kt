package com.github.kimleepark2.domain.entity.file

import com.github.kimleepark2.domain.entity.BaseEntity
import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.user.User
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where

@Comment("파일")
@Where(clause = "deleted_at IS NULL")
@Entity
@Table(name = "tb_file")
class File(
    @Column(name = "path", length = 1000, nullable = false)
    @Comment(value = "사용자 프로필 경로(S3 Path)")
    var path: String,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH]
    )
    @JoinColumn(name = "user_id")
    @Comment(value = "사용자 번호")
    val user: User? = null,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH]
    )
    @JoinColumn(name = "product_id")
    @Comment(value = "상품 번호")
    val product: Product? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "기본키")
    @Column(name = "file_id")
    val id: Long = 0L,
) : BaseEntity()