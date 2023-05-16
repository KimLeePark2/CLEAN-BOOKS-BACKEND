import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.kimleepark2"
version = ""

plugins {
    val kotlinVersion: String by System.getProperties() // 1.7.22
    val springBootVersion: String by System.getProperties() // 3.0.2

    java
    id("org.springframework.boot") version springBootVersion apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0" apply false

    kotlin("jvm") version kotlinVersion apply false                   // 1.7.22
    kotlin("kapt") version kotlinVersion apply false                  // 1.7.22
    kotlin("plugin.spring") version kotlinVersion apply false         // 1.7.22
    kotlin("plugin.jpa") version kotlinVersion apply false            // 1.7.22
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {

    apply {
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")

        plugin("kotlin")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    dependencies {
        val querydslVersion: String by System.getProperties() // 5.0.0
        val springAwsVersion: String by System.getProperties() // 2.2.6.RELEASE

        // kotlin supports module
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//
        // databases
        implementation("org.mariadb.jdbc:mariadb-java-client:3.1.0")

        // aws s3
        implementation("org.springframework.cloud:spring-cloud-starter-aws:${springAwsVersion}")

        // jpa가 사용하는 javax가 jakarta로 변경됨에 따라 아래 어노테이션 프로세서를 추가해줘야 한다.
        annotationProcessor ("jakarta.annotation:jakarta.annotation-api")
        annotationProcessor ("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor ("com.querydsl:querydsl-apt:${querydslVersion}:jakarta")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        disabledRules.set(
            setOf(
                "import-ordering",
                "no-wildcard-imports",
                "final-newline",
                "insert_final_newline",
                "max_line_length"
            )
        )
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // plain jar 기본 true
    tasks.withType<Jar> {
        enabled = true
        // build 중에 중복되는 파일이 생성될경우 에러가 발생한다. 그것을 방지하기 위한 설정이다.
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    // BootJar 기본 false, 프로젝트 빌드 후 실행해야 하는 모듈이면 BootJar true 해줘야 함.
    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }
}