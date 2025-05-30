package com.example.aivle.domain.book.service;

import com.example.aivle.domain.book.dto.CoverRequest;
import com.example.aivle.domain.book.entity.Book;
import com.example.aivle.domain.book.repository.BookRepository;
import com.example.aivle.global.openai.AiCoverClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CoverServiceImpl implements CoverService {

    private final BookRepository bookRepository;
    private final AiCoverClient aiCoverClient;

    @Override
    public String generateCover(CoverRequest req) {
        Book book = bookRepository.save(Book.of(req.title(), req.content()));
        String coverUrl = aiCoverClient.createCover(req.title(), req.content());
        book.attachCover(coverUrl);

        return coverUrl;
    }
}
