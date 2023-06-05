import org.springframework.boot.gradle.tasks.bundling.BootJar

val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
}

dependencies {
    val kapt by configurations

    // // spring-boot-starter
    // spring-boot-starter
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-data-jdbc")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-security")
    // security 일부가 javax로 설정되어있는듯? javax/servlet/http/HttpServletRequest 클래스를 찾을 수 없음 에러가 발생
    // javax.servlet 사용을 위한 라이브러리 - filter에서 사용 9버전까지 사용 가능하며 10버전 이후론 삭제된듯하다.
    api("javax.servlet:javax.servlet-api:4.0.1")

//    api("jakarta.servlet:jakarta.servlet-api:5.0.0")

    api("org.springframework.security:spring-security-oauth2-client:6.0.1") // security.oauth2 사용을 위해 추가
    api("org.springframework.boot:spring-boot-starter-validation") // 파라미터 값 확인(인증, Bean Validation)을 위해
    api("org.springframework.data:spring-data-commons")

    // querydsl, javax -> jakarta로 변경됨에 따라 :jakarta 추가
    api("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    api("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    api("com.querydsl:querydsl-kotlin-codegen:$querydslVersion") // kotlin code generation support
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta") // 이게 없으면 build해도 Q class가 생성되지 않는다.

    // jwt
    api("io.jsonwebtoken:jjwt-gson:0.11.5")
    api("io.jsonwebtoken:jjwt-api:0.11.5")

    // swagger
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // p6spy jdbc logger
    api("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    // Q CLASS 생성을 위해 필요
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<BootJar> {
    enabled = false
}