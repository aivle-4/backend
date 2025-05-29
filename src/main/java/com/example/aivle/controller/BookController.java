package com.example.aivle.controller;

import com.example.aivle.entity.Book;
import com.example.aivle.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updated) {
        return bookService.updateBook(id, updated);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Map.of(
                "isSuccess", true,
                "message", "삭제 완료"
        );
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title) {
        return bookService.searchBooks(title);
    }
}
