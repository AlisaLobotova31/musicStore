package com.example.musicStore.repository;

import com.example.musicStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Order} в базе данных.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Находит все заказы по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список заказов пользователя
     */
    List<Order> findByUserId(Long userId);

    /**
     * Удаляет все заказы по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     */
    void deleteByUserId(Long userId);
}