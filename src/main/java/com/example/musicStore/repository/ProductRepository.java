package com.example.musicStore.repository;

import com.example.musicStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link Product} в базе данных.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}