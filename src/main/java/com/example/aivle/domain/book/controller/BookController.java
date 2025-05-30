package com.example.aivle.domain.book.controller;

import com.example.aivle.domain.book.dto.BookSummaryResponse;
import com.example.aivle.domain.book.service.BookService;
import com.example.aivle.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public Response<List<BookSummaryResponse>> searchBooks(@RequestParam String keyword) {
        List<BookSummaryResponse> response = bookService.searchBooks(keyword);
        return Response.success(response);
    }
}
