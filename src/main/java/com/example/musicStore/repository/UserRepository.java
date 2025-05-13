package com.example.musicStore.repository;

import com.example.musicStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User} в базе данных.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return {@link Optional} с найденным пользователем или пустой, если пользователь не найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты
     * @return {@link Optional} с найденным пользователем или пустой, если пользователь не найден
     */
    Optional<User> findByEmail(String email);
}
