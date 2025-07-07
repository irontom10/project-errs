package net.errs.ProjectErrs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/public/**").permitAll() // Allow public access to /public/**
                .anyRequest().authenticated() // Require authentication for any other request
            .and()
            .formLogin() // Enable form-based login
                .loginPage("/login") // Custom login page
                .permitAll() // Allow everyone to see the login page
            .and()
            .logout() // Enable logout functionality
                .permitAll(); // Allow everyone to log out
        return http.build();
    }
}
