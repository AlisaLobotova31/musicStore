package com.example.musicStore.service;

import com.example.musicStore.model.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для проверки Google reCAPTCHA.
 */
@Service
public class ReCaptchaService {

    /**
     * Секретный ключ для Google reCAPTCHA.
     */
    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;

    /**
     * URL для проверки reCAPTCHA.
     */
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    /**
     * Проверяет ответ reCAPTCHA, отправляя запрос к Google API.
     *
     * @param recaptchaResponse ответ reCAPTCHA от клиента
     * @return true, если проверка успешна, иначе false
     */
    public boolean verify(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + recaptchaSecret + "&response=" + recaptchaResponse;

        try {
            ReCaptchaResponse response = restTemplate.getForObject(url, ReCaptchaResponse.class);
            return response != null && response.isSuccess();
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}