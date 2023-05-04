package com.sparta.write.config;


import com.sparta.write.jwt.JwtAuthFilter;
import com.sparta.write.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.authorizeRequests()
//                .requestMatchers(request -> request.getRequestURI().startsWith("/api/user/")).permitAll()
//                .requestMatchers(request -> request.getRequestURI().equals("/api/search")).permitAll()
//                .requestMatchers(request -> request.getRequestURI().equals("/api/shop")).permitAll()
//                .anyRequest().authenticated()//그외에 모든건 필터거친다 관련(?)
//                // JWT 인증/인가를 사용하기 위한 설정
//                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .requestMatchers(request -> request.getRequestURI().startsWith("/api/user/")).permitAll()
                .requestMatchers(request -> request.getRequestURI().equals("/api/write") && request.getMethod().equals("GET")).permitAll()
                .requestMatchers(request -> request.getRequestURI().startsWith("/api/write/") && !request.getRequestURI().equals("/api/write") &&!request.getMethod().equals("GET")).authenticated()
                .anyRequest().authenticated()//그외에 모든건 필터거친다 관련(?)
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);




        http.formLogin().loginPage("/api/user/login-page").permitAll();

        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");

        return http.build();
    }

}