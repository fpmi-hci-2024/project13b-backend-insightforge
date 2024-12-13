package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.cart.Order;
import com.bookstore.dev.domain.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
