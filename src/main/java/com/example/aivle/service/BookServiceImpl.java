package com.example.aivle.service;

import com.example.aivle.entity.Book;
import com.example.aivle.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book updateBook(Long id, Book updated) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updated.getTitle());
                    book.setAuthor(updated.getAuthor());
                    book.setContent(updated.getContent());
                    book.setCoverImageUrl(updated.getCoverImageUrl());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooks(String title) {
        return bookRepository.findByTitleContaining(title);
    }
}
