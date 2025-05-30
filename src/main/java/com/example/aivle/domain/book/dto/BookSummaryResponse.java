package com.example.aivle.domain.book.dto;

import com.example.aivle.domain.book.entity.Book;

import java.time.LocalDateTime;

public record BookSummaryResponse(Integer bookId,
                                  String title,
                                  String author,
                                  LocalDateTime createdAt,
                                  String coverImageUrl
) {
    public static BookSummaryResponse from(Book book) {
        return new BookSummaryResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getCreatedAt(), book.getCoverImageUrl());
    }
}
