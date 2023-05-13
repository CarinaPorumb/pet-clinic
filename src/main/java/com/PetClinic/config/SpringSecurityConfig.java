package com.PetClinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    //oauth2
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests()
//                .anyRequest().authenticated();
////                .and().oauth2ResourceServer().jwt();
//        return httpSecurity.build();
//    }

    //basic auth
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and().httpBasic(Customizer.withDefaults())
                .csrf().ignoringRequestMatchers("/api/**");
        return http.build();
    }
}