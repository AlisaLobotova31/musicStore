package com.example.musicStore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс промежуточной сущности, представляющий элемент корзины, связывающий корзину и продукт с указанием количества.
 */
@Entity
@Data
@Table(name = "cart_items")
public class CartItem {

    /**
     * Уникальный идентификатор элемента корзины.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Корзина, к которой относится элемент.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    /**
     * Продукт, связанный с элементом корзины.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Количество единиц продукта в корзине.
     */
    private int quantity;

    /**
     * Конструктор по умолчанию.
     */
    public CartItem() {
    }

    /**
     * Конструктор с указанием корзины, продукта и количества.
     *
     * @param cart корзина, к которой относится элемент
     * @param product  продукт, добавленный в корзину
     * @param quantity количество единиц продукта
     */
    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}