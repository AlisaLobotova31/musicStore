package com.example.musicStore.config;

import com.example.musicStore.service.ReCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для проверки reCAPTCHA при попытке входа в систему.
 */
@Component
public class ReCaptchaLoginFilter extends OncePerRequestFilter {

    /**
     * Сервис для проверки reCAPTCHA.
     */
    @Autowired
    private ReCaptchaService reCaptchaService;

    /**
     * Проверяет reCAPTCHA для POST-запросов на /login, перенаправляя на страницу входа при неудачной проверке.
     *
     * @param request HTTP-запрос
     * @param response HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws IOException при ошибках ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        if (request.getRequestURI().equals("/login") && request.getMethod().equalsIgnoreCase("POST")) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            if (recaptchaResponse == null || recaptchaResponse.isEmpty() || !reCaptchaService.verify(recaptchaResponse)) {
                response.sendRedirect("/login.html?error=captcha");
                return;
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}