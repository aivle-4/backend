package com.example.aivle.domain.book.dto;

import com.example.aivle.domain.book.entity.Book;

import java.time.LocalDateTime;

public record BookResponse(Integer bookId, Integer memberId, String author, String title, String content,
                           LocalDateTime createdAt, LocalDateTime updatedAt, String coverImageUrl) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getMember().getId(),
                book.getAuthor(),
                book.getTitle(),
                book.getContent(),
                book.getCreatedAt(),
                book.getUpdatedAt(),
                book.getCoverImageUrl()
        );
    }
}
