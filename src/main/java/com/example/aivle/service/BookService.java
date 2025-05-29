package com.example.aivle.service;

import com.example.aivle.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book saveBook(Book book);
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book updateBook(Long id, Book updated);
    void deleteBook(Long id);
    List<Book> searchBooks(String title);
}

