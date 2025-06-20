package com.mujakjung.global.config;

import com.mujakjung.global.filter.JsonUsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 빈 등록 (AuthenticationConfiguration 이용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Redis 세션 사용시 SecurityContextRepository는 별도 설정 불필요
    // Spring Session Redis가 자동으로 처리

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JsonUsernamePasswordAuthenticationFilter jsonFilter = getJsonUsernamePasswordAuthenticationFilter(
                authenticationManager);

        //경로 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/resource/**")
                        ).permitAll()
                        .requestMatchers("/member/**", "/", "/login").permitAll()
                        .requestMatchers("/adminPage").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll() // 나머지 모든 요청은 허용
                );

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        //form 로그인 방식 disable
        http.formLogin(AbstractHttpConfigurer::disable);

        // 세션 관리 설정 - 세션을 사용하도록 설정
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1) // 동시 세션 수 제한
                .maxSessionsPreventsLogin(false) // 새 로그인 시 기존 세션 무효화
        );

        // Spring Session Redis 사용시 SecurityContext 설정 불필요
        // 자동으로 세션에 저장됨

        http.addFilterAt(jsonFilter, UsernamePasswordAuthenticationFilter.class);

        // ****** 로그아웃 설정 추가 시작 ******
        http
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃을 처리할 URL (기본값: /logout)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 로그아웃 성공 시 처리할 로직
                            SecurityContextHolder.clearContext(); // SecurityContext 명시적으로 클리어
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
                        })
                        .logoutSuccessUrl("/?logout") // 로그아웃 성공 시 리다이렉트할 URL (handler와 동시 사용 불가, handler 우선)
                        .invalidateHttpSession(true) // 세션 무효화 (기본값: true)
                        .deleteCookies("SESSION") // Spring Session Redis 쿠키명
                        .clearAuthentication(true) // 인증 정보 클리어
                );

        return http.build();
    }

    private static JsonUsernamePasswordAuthenticationFilter getJsonUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        JsonUsernamePasswordAuthenticationFilter jsonFilter = new JsonUsernamePasswordAuthenticationFilter(
                authenticationManager);

        jsonFilter.setFilterProcessesUrl("/login");
        jsonFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Spring Session Redis 사용시 세션이 자동으로 Redis에 저장됨
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());



            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"로그인 성공\"}");
        });

        jsonFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"로그인 실패: " + exception.getMessage() + "\"}");
        });

        return jsonFilter;
    }
}