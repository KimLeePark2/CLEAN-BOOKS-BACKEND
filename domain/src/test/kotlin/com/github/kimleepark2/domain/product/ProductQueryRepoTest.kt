package com.github.kimleepark2.domain.product

// @SpringBootTest
// class ProductQueryRepoTest @Autowired constructor(
//    private val userCommand: UserRepository,
//    private val productCommand: ProductRepository,
// ) {

//    fun groupByCountry(): Map<String, List<Product>> {
//        return from(member)
//            .leftJoin(team)
//            .on(member.teamId.eq(team.id))
//            .transform(GroupBy.groupBy<Any>(team.country).`as`(list(member)))
//    }

//    @Test
//    @Transactional
//    fun 프로젝션을_사용하여_나라와_함께_팀의_멤버들을_조회_할_수_있다() {
//        // given
//        createProduct(3, createUser("Korea"))
//        createProduct(3, createUser("Japan"))
//
//        // when
//        val actual: Map<String, List<Product>> = memberRepository.findAllWithCountry()
//            .stream()
//            .collect(
//                Collectors.groupingBy(
//                    MemberByCountryProjection::getCountry, Supplier<M> { HashMap() },
//                    Collectors.mapping(MemberByCountryProjection::getMember, Collectors.toList())
//                )
//            )
//
//        // then
//        assertEquals(actual["Korea"]!!.size, 3)
//        assertEquals(actual["Japan"]!!.size, 3)
//    }

//    @Test
//    @Transactional
//    fun `프로젝션_테스트`() {
//        createProduct(3, createUser("A"))
//        createProduct(3, createUser("B"))
//        productCommand.findAll().forEach {
//            println(it)
//        }
//    }
//
//    private fun createUser(nickname: String): User {
//        val entity = User(
//            username = "username",
//            password = "1",
//            name = "name",
//            nickname = nickname,
//        )
//        return userCommand.save(entity)
//    }
//
//    private fun createProduct(size: Int, user: User): Collection<Product>? {
//        val entities: MutableList<Product> = ArrayList<Product>()
//        for (i in 0 until size) {
//            val entity = Product(
//                title = Faker().book.title(),
//                description = Faker().book.genre(),
//                price = Faker().random.nextInt(),
//                seller = user,
//            )
//            entities.add(productCommand.save(entity))
//        }
//        return entities
//    }
// }