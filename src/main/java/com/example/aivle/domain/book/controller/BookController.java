package com.example.aivle.domain.book.controller;

import com.example.aivle.domain.book.dto.*;
import com.example.aivle.domain.book.service.BookService;
import com.example.aivle.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/cover")
    public Response<CoverResponse> createCover(@Valid @RequestBody CoverRequest req) {
        String url = bookService.generateCover(req);
        return Response.success(new CoverResponse(url));
    }

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
    public Response<BookResponse> createBook(@RequestBody BookRequest request) {
        BookResponse response = bookService.addBook(request);
        return Response.success(response);
    }

    @PutMapping("/{bookId}")
    public Response<BookResponse> updateBook(@PathVariable Integer bookId, @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(bookId, request);
        return Response.success(response);
    }

    @DeleteMapping("/{bookId}")
    public Response<Void> deleteBook(@PathVariable Integer bookId) {
        bookService.deleteBook(bookId);
        return Response.success();
    }

}