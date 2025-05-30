package com.example.aivle.domain.book.controller;

import com.example.aivle.domain.book.dto.CoverRequest;
import com.example.aivle.domain.book.dto.CoverResponse;
import com.example.aivle.domain.book.service.BookService;
import com.example.aivle.domain.book.service.CoverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CoverService coverService;

    @PostMapping("/cover")
    public ResponseEntity<CoverResponse> createCover(@Valid @RequestBody CoverRequest req) {
        String url = coverService.generateCover(req);
        return ResponseEntity.ok(CoverResponse.ok(url));
    }
}
