package com.github.kimleepark2.api.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SwaggerConfig {

    @Bean
    fun openAPI(@Value("\${springdoc.version:0.0.0}") appVersion: String?): OpenAPI {
        val mitLicense = License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/")
        val info = Info()
            .title("책장정리 API")
            .description("김이박2 팀의 책장정리 프로젝트 APIDOC입니다.")
            .version("v0.0.1")
            .contact(Contact().name("개발자 이성복").url("https://github.com/lenope1214").email("dltjdqhr1000@gmail.com"))
            .license(mitLicense)

        val jwtScheme: SecurityScheme = SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .`in`(SecurityScheme.In.HEADER)
            .bearerFormat("JWT")
            .scheme("bearer")

        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "Authorization", jwtScheme
                )
            )
            .info(info)
            .security(listOf(SecurityRequirement().addList("Authorization")))
    }

    @Bean
    fun swaggerCommonGroupApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("공통 API")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/admin/**")
            .build()
    }

    @Bean
    fun swaggerAdminGroupApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("시스템 관리자 API")
            .pathsToMatch("/api/admin/**")
            .build()
    }
}
