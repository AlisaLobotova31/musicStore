package com.example.musicStore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс сущности, представляющий корзину пользователя в приложении музыкального магазина.
 */
@Entity
@Data
@Table(name = "carts")
public class Cart {

    /**
     * Уникальный идентификатор корзины.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, связанный с корзиной.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Список продуктов в корзине.
     */
    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    /**
     * Список элементов корзины с указанием количества.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    /**
     * Конструктор по умолчанию.
     */
    public Cart() {
    }

    /**
     * Конструктор с указанием пользователя.
     *
     * @param user пользователь, связанный с корзиной
     */
    public Cart(User user) {
        this.user = user;
    }

    /**
     * Геттеры и сеттеры для полей класса {@link Cart}.
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Добавляет продукт в корзину, увеличивая количество, если продукт уже присутствует.
     *
     * @param product продукт для добавления
     */
    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        // Если товара нет, добавляем новый CartItem
        CartItem newItem = new CartItem(this, product, 1);
        items.add(newItem);
    }

    /**
     * Удаляет продукт из корзины.
     *
     * @param product продукт для удаления
     */
    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
    }

    /**
     * Очищает все продукты из корзины.
     */
    public void clearProducts() {
        items.clear();
    }
}