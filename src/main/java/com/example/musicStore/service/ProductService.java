package com.example.musicStore.service;

import com.example.musicStore.model.Product;
import com.example.musicStore.repository.CartRepository;
import com.example.musicStore.repository.OrderRepository;
import com.example.musicStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для управления продуктами, включая получение, сохранение и удаление продуктов.
 */
@Service
public class ProductService {

    /**
     * Репозиторий для работы с продуктами.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Репозиторий для работы с корзинами.
     */
    @Autowired
    private CartRepository cartRepository;

    /**
     * Репозиторий для работы с заказами.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Возвращает список всех продуктов.
     *
     * @return список продуктов
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Возвращает продукт по его идентификатору.
     *
     * @param id идентификатор продукта
     * @return продукт или null, если продукт не найден
     */
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    /**
     * Возвращает список уникальных категорий продуктов.
     *
     * @return множество категорий
     */
    public Set<String> getAllCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Возвращает список уникальных брендов продуктов.
     *
     * @return множество брендов
     */
    public Set<String> getAllBrands() {
        return productRepository.findAll()
                .stream()
                .map(Product::getBrand)
                .filter(brand -> brand != null && !brand.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Сохраняет продукт (добавление или обновление).
     *
     * @param product продукт для сохранения
     * @return сохранённый продукт
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Удаляет продукт по идентификатору, предварительно удаляя его из всех корзин и заказов.
     *
     * @param id идентификатор продукта
     */
    public void deleteProduct(Long id) {
        // Удаляем товар из всех корзин
        cartRepository.findAll().forEach(cart -> {
            cart.getProducts().removeIf(product -> product.getId().equals(id));
            cartRepository.save(cart);
        });

        // Удаляем товар из всех заказов
        orderRepository.findAll().forEach(order -> {
            order.getProducts().removeIf(product -> product.getId().equals(id));
            orderRepository.save(order);
        });

        // Удаляем сам товар
        productRepository.deleteById(id);
    }
}