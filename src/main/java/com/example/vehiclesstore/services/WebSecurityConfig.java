package com.example.vehiclesstore.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userDetailsService;

    public WebSecurityConfig(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/login","/checkUser", "/registar", "/registo", "/estatisticas").permitAll()
                        .requestMatchers("/USER").hasRole("USER")
                        .requestMatchers("/ADMIN").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/checkUser")
                        .successHandler((request, response, authentication) -> {
                            Set<String> roles = authentication.getAuthorities().stream()
                                    .map(a -> a.getAuthority())
                                    .collect(Collectors.toSet());

                            HttpSession session = request.getSession();
                            session.setAttribute("email", authentication.getName());
                            if (roles.contains("ROLE_ADMIN")) {
                                response.sendRedirect("/ADMIN");
                            } else {
                                response.sendRedirect("/USER");
                            }

                        })
                        //.defaultSuccessUrl("/USER", true)
                        .permitAll())

                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}