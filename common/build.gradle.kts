
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
    api("org.springframework.security:spring-security-oauth2-client:6.0.1") // security.oauth2 사용을 위해 추가
     api("org.springframework.boot:spring-boot-starter-validation") // 파라미터 값 확인(인증, Bean Validation)을 위해
    api("org.springframework.data:spring-data-commons")

    // jwt
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    api("io.jsonwebtoken:jjwt-gson:0.11.5") // implementation을 api로 변경하면 오류가 발생한다??
    api("io.jsonwebtoken:jjwt-api:0.11.5") // implementation을 api로 변경하면 오류가 발생한다??

    // swagger
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // p6spy jdbc logger
    api("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
}