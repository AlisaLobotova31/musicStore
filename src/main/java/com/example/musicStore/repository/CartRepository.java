package com.example.musicStore.repository;

import com.example.musicStore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Cart} в базе данных.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Находит корзину по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return {@link Optional} с найденной корзиной или пустой, если корзина не найдена
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Удаляет корзину по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     */
    void deleteByUserId(Long userId);
}