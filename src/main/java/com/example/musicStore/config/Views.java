package com.example.musicStore.config;

/**
 * Интерфейсы для определения представлений JSON с помощью Jackson.
 */
public class Views {

    /**
     * Публичное представление, включающее данные, доступные всем.
     */
    public interface Public {}

    /**
     * Внутреннее представление, включающее публичные и дополнительные данные.
     */
    public interface Internal extends Public {}
}