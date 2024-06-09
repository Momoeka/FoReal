package com.image_gallery.image_gallery.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.image_gallery.image_gallery.service.UserService;
import com.image_gallery.image_gallery.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(encoder());

        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            AuthenticationSuccessHandler customSuccess)
            throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/user/register", "/user/signup", "/user/login").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.formLogin(form -> form
                .loginPage("/user/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/authenticateUser")
                .successHandler(customSuccess).permitAll());
        http.logout(logout -> logout.permitAll());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

}
