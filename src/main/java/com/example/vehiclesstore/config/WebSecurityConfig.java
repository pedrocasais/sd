package com.example.vehiclesstore.config;

import com.example.vehiclesstore.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userDetailsService;

    public WebSecurityConfig(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/login","/checkUser", "/registar", "/registo","/estatisticas","/faq","/sobre","/veiculos","/logo", "/imagem/**", "/veiculo/imagem/**").permitAll()
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

                .logout(logout -> logout .logoutSuccessUrl("/logout"));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}