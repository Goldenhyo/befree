package com.project.befree.config;

import com.project.befree.security.JWTCheckFilter;
import com.project.befree.security.handler.CustomLoginFailureHandler;
import com.project.befree.security.handler.CustomLoginSuccessHandler;
import com.project.befree.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CustomSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("********** security config!!");
        http.cors(corsConfigurer -> {
            corsConfigurer.configurationSource(corsConfigurationSource());
        });
        // 세션 stateless(세션 사용하지 않는 상태)로 설정, REST API 는 stateless
        http.sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // csrf 비활성화
        http.csrf(csrf -> csrf.disable());
        // 로그인 설정
        http.formLogin(login -> {
            // 로그인 경로
            login.loginPage("/member/login");
            // 로그인 성공시 실행될 로직 클래스
            login.successHandler(new CustomLoginSuccessHandler(jwtUtil()));
            // 로그인 실패시
            login.failureHandler(new CustomLoginFailureHandler());
        });
        // JWT 체크 필터 추가
        http.addFilterBefore(new JWTCheckFilter(jwtUtil()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // CORS 설정 빈
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 설정 정보
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 리스트형태로 경로 패턴 추가 ( * = 전체)
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        // 위 설정정보를 토대로 Url 전체 경로에 적용하는 소스를 생성해서 리턴
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }
}
