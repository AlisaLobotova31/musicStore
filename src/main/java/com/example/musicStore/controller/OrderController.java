package com.example.musicStore.controller;

import com.example.musicStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для операций с заказами, доступных только пользователям с ролью ADMIN.
 */
@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {

    /**
     * Сервис для работы с заказами.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Возвращает метрики о количестве заказов по пользователям.
     *
     * @return {@link ResponseEntity} со списком метрик (имя пользователя и количество заказов)
     */
    @GetMapping("/metrics/orders-by-user")
    public ResponseEntity<List<Map<String, Object>>> getOrdersCountByUser() {
        List<Map<String, Object>> metrics = orderService.getOrdersCountByUser();
        return ResponseEntity.ok(metrics);
    }

    /**
     * Возвращает метрики об общей стоимости заказов по пользователям.
     *
     * @return {@link ResponseEntity} со списком метрик (имя пользователя и общая стоимость)
     */
    @GetMapping("/metrics/total-price-by-user")
    public ResponseEntity<List<Map<String, Object>>> getTotalPriceByUser() {
        List<Map<String, Object>> metrics = orderService.getTotalPriceByUser();
        return ResponseEntity.ok(metrics);
    }
}