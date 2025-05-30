package com.example.aivle.domain.book.service;

import com.example.aivle.domain.book.dto.BookSummaryResponse;

import java.util.List;

public interface BookService {

    List<BookSummaryResponse> searchBooks(String keyword);
}
