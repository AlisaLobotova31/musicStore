package com.example.musicStore.service;

import com.example.musicStore.model.Cart;
import com.example.musicStore.model.CartItem;
import com.example.musicStore.model.Product;
import com.example.musicStore.model.User;
import com.example.musicStore.repository.CartRepository;
import com.example.musicStore.repository.ProductRepository;
import com.example.musicStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для управления корзиной пользователя, включая добавление/удаление продуктов и очистку корзины.
 */
@Service
public class CartService {

    /**
     * Репозиторий для работы с корзинами.
     */
    @Autowired
    private CartRepository cartRepository;

    /**
     * Репозиторий для работы с пользователями.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Репозиторий для работы с продуктами.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Возвращает корзину пользователя, создавая новую, если она отсутствует.
     *
     * @param userId идентификатор пользователя
     * @return корзина пользователя
     */
    public Cart getCartByUser(Long userId) {
        System.out.println("Getting cart for userId: " + userId);
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            System.out.println("Cart not found, creating new cart for userId: " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            Cart savedCart = cartRepository.save(newCart);
            System.out.println("New cart created with id: " + savedCart.getId());
            return savedCart;
        }
        System.out.println("Cart found with id: " + cartOpt.get().getId());
        return cartOpt.get();
    }

    /**
     * Добавляет продукт в корзину пользователя.
     *
     * @param userId идентификатор пользователя
     * @param productId идентификатор продукта
     * @return обновлённая корзина
     */
    public Cart addProductToCart(Long userId, Long productId) {
        System.out.println("Adding productId: " + productId + " to cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Product found: " + product.getName());
        cart.getProducts().add(product);
        System.out.println("Product added to cart, saving cart...");
        // Добавляем товар в корзину (логика увеличения количества уже в Cart.addProduct)
        cart.addProduct(product);
        System.out.println("Product added to cart, saving cart...");

        Cart savedCart = cartRepository.save(cart);
        System.out.println("Cart saved successfully with id: " + savedCart.getId());
        return savedCart;
    }

    /**
     * Удаляет элемент из корзины пользователя.
     *
     * @param userId идентификатор пользователя
     * @param cartItemId идентификатор элемента корзины
     * @return обновлённая корзина
     */
    public Cart removeProductFromCart(Long userId, Long cartItemId) {
        System.out.println("Removing cartItemId: " + cartItemId + " from cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.getItems().remove(itemToRemove);
        return cartRepository.save(cart);
    }

    /**
     * Очищает корзину пользователя.
     *
     * @param userId идентификатор пользователя
     */
    public void clearCart(Long userId) {
        System.out.println("Clearing cart for userId: " + userId);
        Cart cart = getCartByUser(userId);
        cart.clearProducts();
        cartRepository.save(cart);
    }
}