package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // 로그인 페이지 url 설정
                .loginPage("/members/login")
                // 로그인 성공 시 이동할 URL 설정
                .defaultSuccessUrl("/")
                // 로그인 시 사용할 파라미터 이름으로 email 지정
                .usernameParameter("email")
                // 로그인 실패 시 이동할 URL 설정
                .failureUrl("/members/login/error")
                .and()
                .logout()
                // 로그아웃 URL을 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                // 로그아웃 성공 시 이동할 URL을 설정
                .logoutSuccessUrl("/")
        ;

        http.authorizeRequests()
                // permitAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정
                .mvcMatchers("/","/members/**", "/item/**", "/images/**").permitAll()
                // /admin으로 시작하는 경로는 해당 계정이 admin Role일 경우에만 접근 가능하도록 설정
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                // 위에서 설정해준 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정
                .anyRequest().authenticated();

        http.exceptionHandling()
                //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일은 인증을 무시하도록 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }
}


/*
   HttpSecurity :  http 요청에 대한 보안 설정.  페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성합니다.
   BCryptPasswordEncoder : 비밀번호 암호화
   
   
   Spinrg Security에서 인증은 AuthenticationManger를 통해 이루어지며 AuthenticationManagerBuilder가 AuthenticationManger를 생성
   userDetailsService를 구현하고 있는 객체로 memberService를 지정해주며, 비밀번호 암호화를 위해 passwordEncoder를 지정
*/


