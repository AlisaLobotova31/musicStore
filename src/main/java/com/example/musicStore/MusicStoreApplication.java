package com.example.musicStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения музыкального магазина, запускающий программу.
 */
@SpringBootApplication
public class MusicStoreApplication {

	/**
	 * Точка входа в приложение.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(MusicStoreApplication.class, args);
	}

}