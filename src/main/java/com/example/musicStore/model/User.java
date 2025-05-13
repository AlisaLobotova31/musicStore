package com.example.musicStore.model;

import com.example.musicStore.config.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Класс сущности, представляющий пользователя в приложении музыкального магазина.
 * Реализует интерфейс {@link UserDetails} для интеграции с Spring Security.
 */
@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    /**
     * Имя пользователя, отображаемое в публичных представлениях.
     */
    @JsonView(Views.Public.class)
    private String username;

    /**
     * Пароль пользователя, доступный только во внутренних представлениях.
     */
    @JsonView(Views.Internal.class)
    private String password;

    /**
     * Адрес электронной почты пользователя, отображаемый в публичных представлениях.
     */
    @JsonView(Views.Public.class)
    private String email;

    /**
     * Роль пользователя (USER, ADMIN), отображаемая в публичных представлениях.
     */
    @JsonView(Views.Public.class)
    private String role;

    /**
     * Возвращает полномочия, предоставленные пользователю.
     * Роль пользователя преобразуется в {@link GrantedAuthority}.
     *
     * @return коллекция содержит роль пользователя как GrantedAuthority
     */
    @Override
    @JsonView(Views.Internal.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role);
    }

    /**
     * Проверяет, не истёк ли срок действия учетной записи пользователя.
     *
     * @return true указывает, что срок действия учетной записи не истёк
     */
    @Override
    @JsonView(Views.Internal.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись пользователя.
     *
     * @return true указывает, что учетная запись не заблокирована
     */
    @Override
    @JsonView(Views.Internal.class)
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истёк ли срок действия учетных данных (пароля) пользователя.
     *
     * @return true указывает, что срок действия учетных данных не истёк
     */
    @Override
    @JsonView(Views.Internal.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активна ли учетная запись пользователя.
     *
     * @return true указывает, что учетная запись активна
     */
    @Override
    @JsonView(Views.Internal.class)
    public boolean isEnabled() {
        return true;
    }
}