package com.bookstore.dev.services.entity_services.book;

import com.bookstore.dev.domain.dto.BookResponse;
import com.bookstore.dev.domain.dto.PageWithElements;
import com.bookstore.dev.domain.entities.books.Author;
import com.bookstore.dev.domain.entities.books.Book;
import com.bookstore.dev.domain.entities.books.Genre;
import com.bookstore.dev.domain.repositories.BookRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Optional<BookResponse> getBookById(Long id) {
        return bookRepository.findById(id).map(this::convertToDTO);
    }

    public List<BookResponse> getBooks(Optional<String> title, Optional<String> author,
                                       Optional<List<String>> genres, Optional<String> publisher) {
        Specification<Book> spec = Specification.where(null);

        if (title.isPresent()) {
            spec = spec.and(titleLike(title.get()));
        }
        if (author.isPresent()) {
            spec = spec.and(authorLike(author.get()));
        }
        if (genres.isPresent() && !genres.get().isEmpty()) {
            spec = spec.and(genresIn(genres.get()));
        }

        List<Book> books = bookRepository.findAll(spec);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BookResponse convertToDTO(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor() != null ? book.getAuthor().getName() : null)
                .genres(book.getGenres() != null ?
                        book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()) : null)
                .build();
    }

    private Specification<Book> titleLike(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private Specification<Book> authorLike(String authorName) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Author> author = root.join("author", JoinType.LEFT);
            return criteriaBuilder.equal(criteriaBuilder.lower(author.get("name")), authorName.toLowerCase());
        };
    }

    private Specification<Book> genresIn(List<String> genres) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Genre> genre = root.join("genres", JoinType.LEFT);
            return genre.get("name").in(genres);
        };
    }

    public PageWithElements<BookResponse> getAllBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return new PageWithElements<>(booksPage, booksPage.getContent().stream().map(this::convertToDTO).toList());
    }
}
