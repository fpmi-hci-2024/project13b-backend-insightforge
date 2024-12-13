package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.cart.Cart;
import com.bookstore.dev.domain.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
