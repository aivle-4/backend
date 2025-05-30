package com.example.aivle.domain.book.controller;

import com.example.aivle.domain.book.dto.BookRequest;
import com.example.aivle.domain.book.dto.BookResponse;
import com.example.aivle.domain.book.dto.BookSummaryResponse;
import com.example.aivle.domain.book.service.BookService;
import com.example.aivle.global.response.Response;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public Response<BookResponse> getBookDetails(@PathVariable Integer bookId) {
        BookResponse response = bookService.findBook(bookId);
        return Response.success(response);
    }

    @GetMapping
    public Response<List<BookSummaryResponse>> getBooks(@RequestParam(defaultValue = "") String keyword) {
        List<BookSummaryResponse> response = bookService.findBooks(keyword);
        return Response.success(response);
    }

    @PostMapping
    public Response<BookResponse> createBook(@RequestBody BookRequest request, HttpSession session) {
        BookResponse response = bookService.addBook(request, session);
        return Response.success(response);
    }

    @PutMapping("/{bookId}")
    public Response<BookResponse> updateBook(@PathVariable Integer bookId, @RequestBody BookRequest request, HttpSession session) {
        BookResponse response = bookService.updateBook(bookId, request, session);
        return Response.success(response);
    }

    @DeleteMapping("/{bookId}")
    public Response<Void> deleteBook(@PathVariable Integer bookId, HttpSession session) {
        bookService.deleteBook(bookId, session);
        return Response.success();
    }

}