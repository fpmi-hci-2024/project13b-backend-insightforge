package com.bookstore.dev.services.user_services;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.entities.books.Book;
import com.bookstore.dev.domain.entities.cart.Cart;
import com.bookstore.dev.domain.entities.cart.CartItem;
import com.bookstore.dev.domain.entities.cart.Order;
import com.bookstore.dev.domain.entities.cart.OrderItem;
import com.bookstore.dev.domain.entities.users.User;
import com.bookstore.dev.domain.repositories.BookRepository;
import com.bookstore.dev.domain.repositories.CartRepository;
import com.bookstore.dev.domain.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Transactional
    public Order placeOrder() {
        var user = userService.getUserInContext();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Корзина не найдена"));

        if (cart.getItems().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Корзина пуста");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .build();

        for (CartItem cartItem : cart.getItems()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Недостаточно товара: " + book.getTitle());
            }
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .book(book)
                    .quantity(cartItem.getQuantity())
                    .build();
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public List<Order> getOrderHistory() {
        var user = userService.getUserInContext();
        return orderRepository.findByUser(user);
    }
}
