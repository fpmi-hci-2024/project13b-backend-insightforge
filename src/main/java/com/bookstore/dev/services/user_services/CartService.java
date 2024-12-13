package com.bookstore.dev.services.user_services;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.entities.books.Book;
import com.bookstore.dev.domain.entities.cart.Cart;
import com.bookstore.dev.domain.entities.cart.CartItem;
import com.bookstore.dev.domain.entities.users.User;
import com.bookstore.dev.domain.repositories.BookRepository;
import com.bookstore.dev.domain.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Transactional
    public void addToCart(Long bookId, Integer quantity) {
        var user = userService.getUserInContext();
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Книга не найдена");
        }

        Book book = optionalBook.get();

        if (!isBookAvailable(book)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Книга отсутствует в наличии");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long bookId) {
        var user = userService.getUserInContext();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Корзина не найдена"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Элемент в корзине не найден"));

        cart.getItems().remove(item);
        cartRepository.save(cart);
    }

    private boolean isBookAvailable(Book book) {
        return book.getStock() > 0;
    }
}