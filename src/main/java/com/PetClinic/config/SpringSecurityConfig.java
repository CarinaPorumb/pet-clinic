package com.PetClinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Profile("!test")
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/v1/api-docs**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
             .anyRequest().authenticated());
            //    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt));

        return http.build();
    }


}