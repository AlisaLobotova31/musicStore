package com.example.musicStore.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс сущности, представляющий продукт в приложении музыкального магазина.
 */
@Entity
@Data
@Table(name = "products")
public class Product {

    /**
     * Уникальный идентификатор продукта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    /**
     * Название продукта.
     */
    private String name;

    /**
     * URL изображения продукта.
     */
    private String image;

    /**
     * Категория продукта.
     */
    private String category;

    /**
     * Бренд продукта.
     */
    private String brand;

    /**
     * Цена продукта.
     */
    private double price;

    /**
     * Наличие продукта на складе.
     */
    private boolean inStock;

    /**
     * Описание продукта.
     */
    private String description;

    /**
     * Конструктор по умолчанию.
     */
    public Product() {
    }

    /**
     * Конструктор с указанием всех полей.
     *
     * @param name название продукта
     * @param price цена продукта
     * @param image URL изображения продукта
     * @param category категория продукта
     * @param description описание продукта
     * @param brand бренд продукта
     * @param inStock наличие на складе
     */
    public Product(String name, double price, String image, String category, String description, String brand, boolean inStock) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
        this.description = description;
        this.brand = brand;
        this.inStock = inStock;
    }

    /**
     * Геттеры и сеттеры для полей класса {@link Product}.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}