import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":domain"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

// bootJar 허용, 실행 jar 만들기
tasks.withType<BootJar> {
    enabled = true
}