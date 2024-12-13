package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
}
