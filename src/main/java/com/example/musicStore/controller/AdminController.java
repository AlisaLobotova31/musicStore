package com.example.musicStore.controller;

import com.example.musicStore.model.Product;
import com.example.musicStore.model.User;
import com.example.musicStore.service.ProductService;
import com.example.musicStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для операций, доступных только пользователям с ролью ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    /**
     * Сервис для работы с продуктами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Сервис для работы с пользователями.
     */
    @Autowired
    private UserService userService;

    /**
     * Возвращает список всех пользователей.
     *
     * @return {@link ResponseEntity} со списком пользователей
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param request объект запроса с новой ролью
     * @return {@link ResponseEntity} с результатом операции
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId, @RequestBody RoleUpdateRequest request) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
            }
            user.setRole(request.getRole());
            userService.saveUser(user);
            return ResponseEntity.ok("Роль успешно изменена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при изменении роли: " + e.getMessage());
        }
    }

    /**
     * Удаляет пользователя.
     *
     * @param userId идентификатор пользователя
     * @return {@link ResponseEntity} с результатом операции
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
            }
            userService.deleteUser(userId);
            return ResponseEntity.ok("Пользователь успешно удалён");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    /**
     * Добавляет новый продукт.
     *
     * @param product объект продукта
     * @return {@link ResponseEntity} с сохранённым продуктом или ошибкой
     */
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при добавлении товара: " + e.getMessage());
        }
    }

    /**
     * Обновляет существующий продукт.
     *
     * @param productId идентификатор продукта
     * @param product объект продукта с обновлёнными данными
     * @return {@link ResponseEntity} с обновлённым продуктом или ошибкой
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        try {
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Товар не найден");
            }
            product.setId(productId); // Устанавливаем ID, чтобы обновить существующий товар
            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обновлении товара: " + e.getMessage());
        }
    }

    /**
     * Удаляет продукт.
     *
     * @param productId идентификатор продукта
     * @return {@link ResponseEntity} с результатом операции
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Товар успешно удалён");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении товара: " + e.getMessage());
        }
    }
}

/**
 * Класс запроса для обновления роли пользователя.
 */
class RoleUpdateRequest {

    /**
     * Новая роль пользователя.
     */
    private String role;

    /**
     * Геттеры и сеттеры для поля класса {@link RoleUpdateRequest}.
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}