import org.springframework.boot.gradle.tasks.bundling.BootJar
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

val kotestVersion: String by System.getProperties()

dependencies {
    api(project(":common"))

    val kapt by configurations

    // Q CLASS 생성을 위해 필요
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // kotest
    testApi("io.kotest:kotest-runner-junit5:$kotestVersion")
    testApi("io.kotest:kotest-assertions-core:$kotestVersion")
    testApi("io.kotest:kotest-property:$kotestVersion")
    testApi("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testApi("io.kotest:kotest-framework-datatest:5.3.0")
    testApi("io.kotest.extensions:kotest-extensions-spring:1.1.2")

    // kotlin-faker
    testApi("io.github.serpro69:kotlin-faker:1.11.0")
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<BootJar> {
    enabled = false
}