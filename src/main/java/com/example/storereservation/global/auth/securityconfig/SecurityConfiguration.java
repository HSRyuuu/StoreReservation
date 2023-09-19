package com.example.storereservation.global.auth.securityconfig;

import com.example.storereservation.global.auth.sercurity.AuthenticationFilter;
import com.example.storereservation.global.auth.sercurity.errorHandler.MyAccessDeniedHandler;
import com.example.storereservation.global.auth.sercurity.errorHandler.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final AuthenticationFilter authenticationFilter;

    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers("/partner/**")
                        .hasAuthority("ROLE_PARTNER")
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(myAuthenticationEntryPoint)
                        .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/logout/success")
                        .invalidateHttpSession(true)
                .and()
                    .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 개발용
        if(Arrays.asList(environment.getActiveProfiles()).contains("dev")){
            http.authorizeRequests().antMatchers("/**").permitAll();
        }

    }

    @Override
    public void configure(final WebSecurity webSecurity){
        webSecurity.ignoring()
                .antMatchers("/**/register", "/**/login", "/exception/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
