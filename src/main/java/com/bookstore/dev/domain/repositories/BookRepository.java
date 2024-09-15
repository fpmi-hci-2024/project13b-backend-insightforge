package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
