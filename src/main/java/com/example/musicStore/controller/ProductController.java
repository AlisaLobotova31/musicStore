package com.example.musicStore.controller;

import com.example.musicStore.model.Product;
import com.example.musicStore.service.ProductService;
import com.example.musicStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Контроллер для управления операциями с продуктами, доступными публично.
 */
@RestController
@RequestMapping("/api/public/products")
public class ProductController {

    /**
     * Сервис для работы с продуктами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Возвращает список всех продуктов.
     *
     * @return список продуктов
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Возвращает продукт по его идентификатору.
     *
     * @param id идентификатор продукта
     * @return {@link ResponseEntity} с продуктом или ошибкой
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Товар не найден");
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении товара: " + e.getMessage());
        }
    }

    /**
     * Возвращает список всех уникальных категорий продуктов.
     *
     * @return множество категорий
     */
    @GetMapping("/categories")
    public Set<String> getAllCategories() {
        return productService.getAllCategories();
    }

    /**
     * Возвращает список всех уникальных брендов продуктов.
     *
     * @return множество брендов
     */
    @GetMapping("/brands")
    public Set<String> getAllBrands() {
        return productService.getAllBrands();
    }

    /**
     * Создаёт новый продукт.
     *
     * @param product объект продукта для сохранения
     * @return сохранённый продукт
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}