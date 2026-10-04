
package com.rizki.book_management.controller;

import com.rizki.book_management.entity.Book;
import com.rizki.book_management.exception.BookNotFoundException;
import com.rizki.book_management.exception.InvalidRequestException;
import com.rizki.book_management.service.BookService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
    
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final Validator validator;

    public BookController(BookService bookService, Validator validator) {
        this.bookService = bookService;
        this.validator = validator;
    }

    // get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // get book by id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // add new book
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBook);
    }

    // Full allow update all book info by id
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book book) {

        return bookService.getBookById(id)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setAuthor(book.getAuthor());
                    existingBook.setBookNumber(book.getBookNumber());
                    existingBook.setPublicationYear(book.getPublicationYear());

                    Book updatedBook = bookService.saveBook(existingBook);

                    return ResponseEntity.ok(updatedBook);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // partial update a book
    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new InvalidRequestException("At least one book field must be provided.");
        }

        return bookService.getBookById(id)
                .map(existingBook -> {
                    for (String field : updates.keySet()) {
                        if (!Set.of("title", "author", "bookNumber", "publicationYear").contains(field)) {
                            throw new InvalidRequestException("Unknown book field: " + field + ".");
                        }
                    }
                    if (updates.containsKey("title")) {
                        existingBook.setTitle(stringValue(updates, "title"));
                    }

                    if (updates.containsKey("author")) {
                        existingBook.setAuthor(stringValue(updates, "author"));
                    }

                    if (updates.containsKey("bookNumber")) {
                        existingBook.setBookNumber(stringValue(updates, "bookNumber"));
                    }

                    if (updates.containsKey("publicationYear")) {
                        Object year = updates.get("publicationYear");

                        existingBook.setPublicationYear(
                                year == null
                                        ? null
                                        : integerValue(year, "publicationYear"));
                    }

                    Set<ConstraintViolation<Book>> violations = validator.validate(existingBook);
                    if (!violations.isEmpty()) {
                        throw new ConstraintViolationException(violations);
                    }

                    Book updatedBook = bookService.saveBook(existingBook);

                    return ResponseEntity.ok(updatedBook);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isEmpty()) {
            throw new BookNotFoundException(id);
        }

        bookService.deleteBook(id);

        return ResponseEntity.ok(
                Map.of("message", "Book deleted successfully.")
        );
    }

    private String stringValue(Map<String, Object> updates, String field) {
        Object value = updates.get(field);
        if (value != null && !(value instanceof String)) {
            throw new InvalidRequestException(field + " must be a string.");
        }
        return (String) value;
    }

    private Integer integerValue(Object value, String field) {
        if (!(value instanceof Number)) {
            throw new InvalidRequestException(field + " must be an integer.");
        }
        try {
            return new BigDecimal(value.toString()).intValueExact();
        } catch (NumberFormatException | ArithmeticException exception) {
            throw new InvalidRequestException(field + " must be an integer.");
        }
    }
}