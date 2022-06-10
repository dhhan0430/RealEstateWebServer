package com.fastcampus.jpa.bookmanager2.domain;

import com.fastcampus.jpa.bookmanager2.domain.listener.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
// @EntityListeners(value = AuditingEntityListener.class)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Long authorId;

    // private Long publisherId;

    // Book 과 BookReviewInfo 가 서로 OneToOne 걸면 양방향 관계가 된다.
    // 그러나 mappedBy 를 거는 순간, 양방향 관계에서 주인이 생기게 된다.
    // FK 를 갖는 테이블이 연관 관계에서 주인이 된다.
    // 주인이 bookReviewInfo
    // 근데 여기에 mappedBy를 걸면 순환참조로 인해 스택오버플로우가 발생한다.
    // mappedBy를 안 걸면 스택오버플로우가 발생 안함.. (아마 bookReviewInfo 가 null이어서..?
    // 근데 mappedBy를 걸어도 bookReviewInfo는 null 이다...)
    // mappedBy를 걸면 book 입장에선 bookReviewInfo 가 안 보인다는데...
    // 정리를 다시 하자면, @OneToOne 을 걸면 select가 한 번 더 실행된다.
    // mappedBy를 걸면 해당 FK가 보이지 않는다. 그래서 그에 대한 참조가
    // 일어나지 않을 것 같은데.. ToString 대상에서 제외를 안시키면, 왜 에러가 발생하는가?
    // 그냥 여기에 @OneToOne 만 걸면 book에서는 bookReviewInfo 가 null로 조회 되는데,
    // mappedBy를 거는 순간 BookReviewInfo field를
    // 채우지 않더라도 query 조회 시 어디와 엮여 있는지 알 수 있다.
    // 이는 아마도, bookReviewInfo 쪽에서 여기(book)을 참조해놓았기 때문인 것 같다.
    // mappedBy를 걸면 하인 입장에서는 주인의 FK 가 보이진 않는다.
    // 하지만, 주인을 가리키는 field를 직접적으로 채우지 않더라도 mappedBy 로 인해,
    // 주인 값을 참조할 수 있다. 그래서 서로 toString에 대한 순환 참조가 발생하게 된다.
    @OneToOne(mappedBy = "book")
    @ToString.Exclude
    private BookReviewInfo bookReviewInfo;

    @OneToMany
    // OneToMany는 중간 매핑 테이블이 생성되기 때문에, JoinColumn 을 적용하여 생성을 막는다.
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne
    // @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    private Publisher publisher;

    // 보통 현업에서는 ManyToMany 방식을 사용하지 않는다. (복잡성이 크기 때문에)
    // Book(N:1) <-> 중간 entity <-> Author(1:N) 의 형식으로 설계한다.
    /*
    @ManyToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Author> authors = new ArrayList<>();
    */
    /*
    public void addAuthor(Author... author) {
        Collections.addAll(this.authors, author);
    }
    */

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    public void addBookAndAuthors(BookAndAuthor... bookAndAuthors) {
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }

    // private String author;
    /*
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    */
    /*
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
    */
}
