package com.example.musicStore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация безопасности приложения с использованием Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Фильтр для проверки reCAPTCHA при входе.
     */
    @Autowired
    private ReCaptchaLoginFilter reCaptchaLoginFilter;

    /**
     * Настраивает цепочку фильтров безопасности.
     *
     * @param http объект для конфигурации HTTP-безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception при ошибках конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(reCaptchaLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/verify-registration.html", "/css/**", "/js/**", "/images/**", "/api/public/**", "/api/users/register", "/api/users/verify-registration").permitAll()
                        .requestMatchers("/profile.html", "/author.html", "/cart.html", "/api/users/me", "/api/users/change-password", "/api/cart/**").authenticated()
                        .requestMatchers("/admin/**", "/admin.html").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html?success=true", true)
                        .failureUrl("/login.html?error=true")
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/login.html?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .expiredUrl("/login.html?expired=true")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                Map<String, String> error = new HashMap<>();
                                error.put("message", "Пожалуйста, авторизуйтесь, чтобы добавить товар в корзину.");
                                new ObjectMapper().writeValue(response.getOutputStream(), error);
                            } else {
                                response.sendRedirect("/login.html");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                Map<String, String> error = new HashMap<>();
                                error.put("message", "Доступ запрещён");
                                new ObjectMapper().writeValue(response.getOutputStream(), error);
                            } else {
                                response.sendRedirect("/index.html");
                            }
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Настраивает игнорирование статических ресурсов для безопасности.
     *
     * @return объект настройки безопасности веб-ресурсов
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**");
    }

    /**
     * Предоставляет кодировщик паролей BCrypt.
     *
     * @return объект кодировщика паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}