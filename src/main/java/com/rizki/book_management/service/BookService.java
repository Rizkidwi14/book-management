package com.rizki.book_management.service;

import com.rizki.book_management.entity.Book;
import com.rizki.book_management.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // get one book by id
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    //add or update a book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    //delete a book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
