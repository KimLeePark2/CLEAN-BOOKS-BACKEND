val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")

    // intellij idea에서 사용할 수 있도록 추가
    idea
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

noArg {
    annotation("jakarta.persistence.Entity") // @Entity가 붙은 클래스에 한해서만 no arg 플러그인을 적용
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    api(project(":common"))

    val kapt by configurations

    api("org.springframework.boot:spring-boot-starter-actuator:3.0.1")

    // jakarta.servlet 사용을 위한 라이브러리 - filter에서 사용 9버전까지 사용 가능하며 10버전 이후론 삭제된듯하다.
//    api("org.apache.tomcat.embed:tomcat-embed-core:9.0.65")



    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")

    // querydsl javax -> jakarta로 변경됨에 따라 :jakarta 추가
    api("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    api("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    api("com.querydsl:querydsl-kotlin-codegen:$querydslVersion") // kotlin code generation support
    // javax -> jakarta로 변경을 위해 :jpa -> :jakarta
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta") // 이게 없으면 build해도 Q class가 생성되지 않는다.

    kapt("org.springframework.boot:spring-boot-configuration-processor")
}