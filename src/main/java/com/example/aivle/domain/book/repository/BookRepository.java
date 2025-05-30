package com.example.aivle.domain.book.repository;

import com.example.aivle.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
        SELECT b
        FROM Book b
        WHERE LOWER(b.title)  LIKE LOWER(CONCAT('%', :kw, '%'))
           OR LOWER(b.author) LIKE LOWER(CONCAT('%', :kw, '%'))
    """)
    List<Book> searchByKeyword(@Param("kw") String keyword);
}

