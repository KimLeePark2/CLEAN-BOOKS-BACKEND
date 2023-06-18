package com.github.kimleepark2.domain.entity.wish

import com.github.kimleepark2.domain.entity.BaseEntity
import com.github.kimleepark2.domain.entity.product.Product
import com.github.kimleepark2.domain.entity.user.User
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where
import org.hibernate.annotations.WhereJoinTable

@Comment("찜")
@Where(clause = "deleted_at IS NULL")
@Entity
@Table(name = "tb_wish")
class Wish(
    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH]
    )
    @JoinColumn(name = "product_id", foreignKey = ForeignKey(name = "fk_wish_product_id"))
    @WhereJoinTable(clause = "deleted_at IS NULL")
    @Comment(value = "상품 번호")
    val product: Product,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH]
    )
    @JoinColumn(name = "user_email", foreignKey = ForeignKey(name = "fk_wish_user_email"))
    @WhereJoinTable(clause = "deleted_at IS NULL")
    @Comment(value = "회원 번호")
    val user: User,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "기본키")
    @Column(name = "wish_id")
    val id: Long = 0L,
) : BaseEntity()
