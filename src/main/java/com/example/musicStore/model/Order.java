package com.example.musicStore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс сущности, представляющий заказ пользователя в приложении музыкального магазина.
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Уникальный идентификатор заказа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, сделавший заказ.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Список продуктов в заказе.
     */
    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    /**
     * Дата и время создания заказа.
     */
    private LocalDateTime orderDate;

    /**
     * Общая стоимость заказа.
     */
    private double totalPrice;

    /**
     * Конструктор по умолчанию.
     */
    public Order() {
    }

    /**
     * Конструктор с указанием пользователя, даты заказа и общей стоимости.
     *
     * @param user пользователь, сделавший заказ
     * @param orderDate  дата и время заказа
     * @param totalPrice общая стоимость заказа
     */
    public Order(User user, LocalDateTime orderDate, double totalPrice) {
        this.user = user;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    /**
     * Геттеры и сеттеры для полей класса {@link Order}.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Добавляет продукт в заказ.
     *
     * @param product продукт для добавления
     */
    public void addProduct(Product product) {
        this.products.add(product);
    }

    /**
     * Удаляет продукт из заказа.
     *
     * @param product продукт для удаления
     */
    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}