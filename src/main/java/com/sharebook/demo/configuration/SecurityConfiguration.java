package com.sharebook.demo.configuration;


import com.sharebook.demo.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {



    @Autowired
    JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
       http
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/users").permitAll()
                       .requestMatchers("/authenticate").permitAll()
                       .requestMatchers("/isConnected").permitAll()
                       .requestMatchers("/v3/api-docs/**").permitAll()
                       .requestMatchers("/api-docs.yaml").permitAll()
                       .requestMatchers("/swagger-resources/**").permitAll()
                       .requestMatchers("/swagger-ui/**").permitAll()
                       .requestMatchers("/swagger-ui.html").permitAll()
                       .requestMatchers("/webjars/**").permitAll()
                       .anyRequest().authenticated()

               );
         http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

}
