package com.mazino.springboot.config.auth;

import com.mazino.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //Spring security 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console 화면 사용하기 위해서는 disable 필요
                .and()
                .authorizeRequests()// URL별 권한관리 시작.
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()//권한관리 대상,
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())// api/v1/권한은 USER 권한 사용자만
                .anyRequest().authenticated() //그외 나머지 url, authenticated는 인증된(로그인된) 사용자에게
                .and()
                .logout()
                .logoutSuccessUrl("/") //로그아웃시 진입점
                .and()
                .oauth2Login() //oauth 진입
                .userInfoEndpoint()//사용자 정보 가져오는 설정
                .userService(customOAuth2UserService);
    }
}