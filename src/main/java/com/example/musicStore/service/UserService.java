package com.example.musicStore.service;

import com.example.musicStore.model.User;
import com.example.musicStore.repository.CartRepository;
import com.example.musicStore.repository.UserRepository;
import com.example.musicStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления пользователями, включая загрузку данных для аутентификации и операции CRUD.
 * Реализует интерфейс {@link UserDetailsService} для интеграции с Spring Security.
 */
@Service
public class UserService implements UserDetailsService {

    /**
     * Репозиторий для работы с пользователями.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Репозиторий для работы с корзинами пользователей.
     */
    @Autowired
    private CartRepository cartRepository;

    /**
     * Репозиторий для работы с заказами пользователей.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Кодировщик паролей для шифрования.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Загружает данные пользователя по логину (имя пользователя или email) для аутентификации.
     *
     * @param login логин пользователя (имя пользователя или email)
     * @return {@link UserDetails} с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("Loading user by login: " + login);

        // Проверяем, является ли login email'ом (содержит @)
        Optional<User> userOpt;
        if (login.contains("@")) {
            userOpt = userRepository.findByEmail(login);
        } else {
            userOpt = userRepository.findByUsername(login);
        }

        if (userOpt.isEmpty()) {
            System.out.println("User not found: " + login);
            throw new UsernameNotFoundException("User not found: " + login);
        }
        User user = userOpt.get();
        System.out.println("User found: " + user.getUsername() + ", role: " + user.getRole());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }


    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return найденный пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или null, если пользователь не найден
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Сохраняет пользователя без изменения пароля.
     *
     * @param user пользователь для сохранения
     * @return сохранённый пользователь
     */
    public User saveUser(User user) {
        // Сохраняем пользователя без изменения пароля
        return userRepository.save(user);
    }

    /**
     * Сохраняет пользователя с шифрованием пароля, если он указан.
     *
     * @param user пользователь для сохранения
     * @return сохранённый пользователь
     */
    public User saveUserWithPassword(User user) {
        // Шифруем пароль, если он указан
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя и связанные с ним данные (корзину и заказы).
     *
     * @param userId идентификатор пользователя
     */
    @Transactional
    public void deleteUser(Long userId) {
        // Очищаем корзину пользователя перед удалением
        cartRepository.deleteByUserId(userId);
        orderRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    /**
     * Возвращает идентификатор пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return идентификатор пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getId();
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email адрес электронной почты
     * @return true, если email уже используется, иначе false
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Возвращает кодировщик паролей.
     *
     * @return кодировщик паролей
     */
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}