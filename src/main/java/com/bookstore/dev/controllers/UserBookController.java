package com.bookstore.dev.controllers;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.dto.BookResponse;
import com.bookstore.dev.services.entity_services.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@SecurityScheme(
        name = "tokenAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization",
        description = "Token-based Authentication"
)
public class UserBookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "tokenAuth")})
    public ResponseEntity<?> getBookById(@PathVariable("id") @Min(1) Long id) {
        Optional<BookResponse> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return ok(book.get());
        } else {
            throw new ApiException(HttpStatus.NOT_FOUND, "Такой книги не найдено");
        }
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "tokenAuth")})
    public ResponseEntity<?> getBooks(
            @RequestParam Optional<@Size(min = 3) String> title,
            @RequestParam Optional<@Size(min = 1) String> author,
            @RequestParam Optional<List<String>> genres,
            @RequestParam Optional<String> publisher
    ) {
        if (title.isPresent() && title.get().length() < 3) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Название книги должно содержать не менее 3х символов.");
        }

        List<BookResponse> books = bookService.getBooks(title, author, genres, publisher);
        if (books.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Книг по вашему запросу не найдено.");
        }
        return ok(books);
    }

    @GetMapping("/by-page")
    @Operation(security = {@SecurityRequirement(name = "tokenAuth")})
    public ResponseEntity<?> getBooksByPage(@RequestParam Integer page, @RequestParam Integer size) {
        return ok(bookService.getAllBooksPaginated(page, size));
    }
}
