package com.example.aivle.domain.book.service;

import com.example.aivle.domain.book.dto.BookRequest;
import com.example.aivle.domain.book.dto.BookResponse;
import com.example.aivle.domain.book.dto.BookSummaryResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface BookService {

    // 도서 조회
    BookResponse findBook(Integer bookId);

    // 도서 목록 조회(키워드 = "" -> 전체 검색)
    List<BookSummaryResponse> findBooks(String keyword);

    // 도서 추가
    BookResponse addBook(BookRequest request, HttpSession session);

    // 도서 수정
    BookResponse updateBook(Integer bookId, BookRequest request, HttpSession session);

    // 도서 삭제
    void deleteBook(Integer bookId, HttpSession session);
}
