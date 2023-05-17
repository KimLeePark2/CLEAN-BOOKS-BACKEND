package com.github.kimleepark2.api.config

import com.github.kimleepark2.common.jwt.JwtTokenProvider
import com.github.kimleepark2.common.jwt.filter.JwtAuthenticationFilter
import com.github.kimleepark2.common.jwt.filter.JwtExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록. 필터체인(묶음)에 필터등록
@EnableMethodSecurity(
    securedEnabled = true, // Controller 에서 @Secured 어노테이션 사용가능. @Secured("roleName")
    prePostEnabled = true // preAuthorize 어노테이션 활성화
)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtExceptionFilter: JwtExceptionFilter,
) {

    @Bean // 더블 슬래쉬 허용
    fun defaultHttpFirewall(): HttpFirewall {
        return DefaultHttpFirewall()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.httpBasic().disable()
            .csrf().disable()
            .cors().disable()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeHttpRequests()
            // 모든 요청 처리 허용
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .disable()
            // oauth2 로그인 설정 추가
//            .logout()
//            .logoutSuccessUrl("/")
//            .and()
//            .oauth2Login()

        http
            // jwt 토큰 필터
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            // jwt 토큰 필터에서 나온 에러 처리용 필터
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)

        return http.build()
    }
}
