package com.example.aivle.domain.book.service;

import com.example.aivle.domain.book.dto.BookSummaryResponse;
import com.example.aivle.domain.book.entity.Book;
import com.example.aivle.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookSummaryResponse> findBooks(String keyword) {
        List<Book> books = bookRepository.searchByKeyword(keyword);
        return books.stream().map(BookSummaryResponse::from).collect(Collectors.toList());
    }
}
