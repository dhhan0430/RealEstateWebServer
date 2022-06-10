package com.fastcampus.jpa.bookmanager2.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Author extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String country;

    // @OneToMany 에서는 @JoinColumn 을 통해 중간 매핑 테이블을 제거할 수 있었는데,
    // ManyToMany에서는 그럴 수 없다.
    /*
    @ManyToMany
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();
    */
    /*
    public void addBook(Book... book) {
        Collections.addAll(this.books, book);
    }
    */
    @OneToMany
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    public void addBookAndAuthors(BookAndAuthor... bookAndAuthors) {
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }


}
