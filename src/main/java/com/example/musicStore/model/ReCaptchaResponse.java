package com.example.musicStore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс, представляющий ответ от сервиса Google reCAPTCHA.
 */
public class ReCaptchaResponse {

    /**
     * Успешность проверки reCAPTCHA.
     */
    private boolean success;

    /**
     * Временная метка вызова reCAPTCHA.
     */
    private String challengeTs;

    /**
     * Имя хоста, на котором выполнялась проверка.
     */
    private String hostname;

    /**
     * Коды ошибок, возвращённые при проверке.
     */
    @JsonProperty("error-codes")
    private String[] errorCodes;

    /**
     * Геттеры и сеттеры для полей класса {@link ReCaptchaResponse}.
     */
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }
}