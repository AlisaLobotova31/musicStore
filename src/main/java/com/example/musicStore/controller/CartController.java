package com.example.musicStore.controller;

import com.example.musicStore.model.Cart;
import com.example.musicStore.model.Order;
import com.example.musicStore.service.CartService;
import com.example.musicStore.service.OrderService;
import com.example.musicStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления операциями с корзиной, такими как добавление/удаление продуктов и оформление заказов.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    /**
     * Сервис для работы с корзиной.
     */
    @Autowired
    private CartService cartService;

    /**
     * Сервис для работы с заказами.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Сервис для работы с пользователями.
     */
    @Autowired
    private UserService userService;

    /**
     * Возвращает корзину текущего аутентифицированного пользователя.
     *
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return корзина пользователя
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            return ResponseEntity.ok(cartService.getCartByUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Добавляет продукт в корзину пользователя.
     *
     * @param productId идентификатор продукта
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return {@link ResponseEntity} с обновлённой корзиной или ошибкой
     */
    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Cart cart = cartService.addProductToCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding product to cart: " + e.getMessage());
        }
    }

    /**
     * Удаляет элемент из корзины пользователя.
     *
     * @param cartItemId идентификатор элемента корзины
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return {@link ResponseEntity} с обновлённой корзиной или ошибкой
     */
    @DeleteMapping("/remove/{cartItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long cartItemId, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Cart cart = cartService.removeProductFromCart(userId, cartItemId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Очищает корзину пользователя.
     *
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return {@link ResponseEntity} с результатом операции
     */
    @DeleteMapping("/clear")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            cartService.clearCart(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Оформляет заказ на основе корзины пользователя.
     *
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return созданный заказ
     */
    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> checkout(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            return ResponseEntity.ok(orderService.createOrder(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Возвращает историю заказов текущего пользователя.
     *
     * @param authentication объект аутентификации, содержащий данные пользователя
     * @return список заказов пользователя
     */
    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getOrders(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            List<Order> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Получает идентификатор пользователя из объекта аутентификации.
     *
     * @param authentication объект аутентификации
     * @return идентификатор пользователя
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserIdByUsername(username);
    }
}