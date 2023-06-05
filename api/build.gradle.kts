import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.security:spring-security-test:5.7.3")
}

tasks.withType<Jar> {
    enabled = false
}

// bootJar 허용, 실행 jar 만들기
tasks.withType<BootJar> {
    enabled = true
}