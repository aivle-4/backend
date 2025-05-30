package com.example.aivle.domain.book.entity;

import com.example.aivle.domain.member.entity.Member;
import com.example.aivle.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String title;
    private String author;
    private String content;

    @Column(length = 2048)  // 글자수 제한 오류 수정
    private String coverImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Book(String title, String content) {
        super();
    }

    public static Book of(String title, String content) {
        return new Book(title, content);
    }

    public void attachCover(String url) {
        this.coverImageUrl = url;
    }
}
