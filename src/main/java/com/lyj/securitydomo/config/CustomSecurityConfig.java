package com.lyj.securitydomo.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    // UserDetailsService는 사용자 인증 정보를 가져오는 서비스로, Security 설정에 사용
    private final UserDetailsService userDetailsService;

    // SecurityFilterChain을 설정하여 각 요청의 보안 규칙을 정의
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스 접근 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 특정 경로 접근 허용
                        .requestMatchers("/login", "/signup", "/replies/**", "/user/**", "/", "/all", "/posting/**", "/view/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/report/create").permitAll() // 신고 기능
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // 관리자만 접근 가능
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/user/login") // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/loginProcess") // 로그인 처리 URL
                        .defaultSuccessUrl("/posting/list") // 로그인 성공 시 이동할 URL
                        .failureUrl("/user/login?error=true") // 로그인 실패 시 이동할 URL
                        .permitAll() // 로그인 페이지 접근 허용
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll() // 로그아웃은 누구나 접근 가능
                );

        // 사용자 인증 관리 설정
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

        return http.build();
    }

    // BCryptPasswordEncoder 빈 설정으로 비밀번호 암호화 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}