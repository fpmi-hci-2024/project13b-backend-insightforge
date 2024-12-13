package com.bookstore.dev.domain.repositories;


import com.bookstore.dev.domain.entities.books.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);
}
