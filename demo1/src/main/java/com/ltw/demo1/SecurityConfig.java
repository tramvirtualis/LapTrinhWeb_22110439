package com.ltw.demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Khai báo user cứng để test
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}123456") // {noop} = không mã hoá password
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // Cấu hình filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/hello").permitAll()      // ai cũng vào được
                        .requestMatchers("/customers").authenticated() // cần login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll() // cho phép truy cập static resources
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .permitAll()   // sử dụng trang login mặc định của Spring Security
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
