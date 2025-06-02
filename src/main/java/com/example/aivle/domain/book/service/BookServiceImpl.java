package com.example.aivle.domain.book.service;

import com.example.aivle.domain.book.dto.BookRequest;
import com.example.aivle.domain.book.dto.BookResponse;
import com.example.aivle.domain.book.dto.BookSummaryResponse;
import com.example.aivle.domain.book.dto.CoverRequest;
import com.example.aivle.domain.book.entity.Book;
import com.example.aivle.domain.book.repository.BookRepository;
import com.example.aivle.domain.member.entity.Member;
import com.example.aivle.domain.member.service.MemberService;
import com.example.aivle.global.openai.AiCoverClient;
import com.example.aivle.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.aivle.global.response.ErrorCode.NOT_AVAILABLE_BOOK;
import static com.example.aivle.global.response.ErrorCode.NOT_FOUND_BOOK;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MemberService memberService;
    private final AiCoverClient aiCoverClient;

    @Transactional(readOnly = true)
    @Override
    public BookResponse findBook(Integer bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));
        return BookResponse.from(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookSummaryResponse> findBooks(String keyword) {
        List<Book> books = bookRepository.searchByKeyword(keyword);
        return books.stream().map(BookSummaryResponse::from).collect(Collectors.toList());
    }

    @Override
    public BookResponse addBook(BookRequest bookRequest) {
        Member member = memberService.findMember();

        Book book = Book.builder()
                .member(member)
                .title(bookRequest.title())
                .author(bookRequest.author())
                .content(bookRequest.content())
                .coverImageUrl(bookRequest.coverImageUrl())
                .build();

        Book savedBook = bookRepository.save(book);
        return BookResponse.from(savedBook);
    }

    @Override
    public BookResponse updateBook(Integer bookId, BookRequest request) {
        Integer memberId = memberService.findMember().getId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));

        if (!book.getMember().getId().equals(memberId)) {
            throw new CustomException(NOT_AVAILABLE_BOOK, "수정 권한이 없습니다.");
        }

        book.update(request.title(), request.author(), request.content(), request.coverImageUrl());

        return BookResponse.from(book);
    }

    @Override
    public void deleteBook(Integer bookId) {
        Integer memberId = memberService.findMember().getId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));

        if (!book.getMember().getId().equals(memberId)) {
            throw new CustomException(NOT_AVAILABLE_BOOK, "삭제 권한이 없습니다.");
        }

        bookRepository.delete(book);
    }

    @Override
    public String generateCover(CoverRequest req) {
        return aiCoverClient.createCover(req.title(), req.content());
    }
}
