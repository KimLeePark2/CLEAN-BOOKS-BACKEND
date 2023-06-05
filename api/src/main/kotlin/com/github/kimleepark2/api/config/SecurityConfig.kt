package com.github.kimleepark2.api.config

import com.github.kimleepark2.common.jwt.JwtTokenProvider
import com.github.kimleepark2.common.jwt.filter.JwtAuthenticationFilter
import com.github.kimleepark2.common.jwt.filter.JwtExceptionFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록. 필터체인(묶음)에 필터등록
@EnableMethodSecurity(
    securedEnabled = true, // Controller 에서 @Secured 어노테이션 사용가능. @Secured("roleName")
    prePostEnabled = true // preAuthorize 어노테이션 활성화
)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtExceptionFilter: JwtExceptionFilter,
//    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
//    private val userOAuth2Service: UserOAuth2Service,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${jwt.paths:}")
    val validPaths: List<String> = listOf()

    @Bean // 더블 슬래쉬 허용
    fun defaultHttpFirewall(): HttpFirewall {
        return DefaultHttpFirewall()
    }

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        passwordEncoder: BCryptPasswordEncoder?,
        userDetailsService: UserDetailsService?,
    ): AuthenticationManager? {
        return http.getSharedObject<AuthenticationManagerBuilder>(AuthenticationManagerBuilder::class.java)
            .userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.httpBasic().disable()
            .csrf().disable()
            .cors().disable()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // application.yml의 jwt.paths에 있는 경로는 인증필요
        // anyRequest 인증 처리 전에 있어야 함.
        validPaths.forEach { path ->
            log.info("valid path: {}", path)
            // validPaths에 해당하는 요청은 인증이 필요한 요청이므로 필터링 되도록 설정한다.
            // springboot 3 버전부터 변경됨
            http.authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(AntPathRequestMatcher("/openapi/openapi.yml")).authenticated()
            }
        }

        // jwt 처리 인증 처리에서 걸러내지 않은 모든 요청은 허용한다.
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
//            // 로그인 성공 시 설정 추가
//            .defaultSuccessUrl("/login-success")
//            .successHandler(oAuth2AuthenticationSuccessHandler)
//            .userInfoEndpoint()
//            .userService(userOAuth2Service)

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
