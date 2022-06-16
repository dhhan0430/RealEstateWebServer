package com.fastcampus.jpa.bookmanager2.domain;

import com.fastcampus.jpa.bookmanager2.domain.converter.BookStatusConverter;
import com.fastcampus.jpa.bookmanager2.domain.listener.Auditable;
import com.fastcampus.jpa.bookmanager2.repository.dto.BookStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
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
// isolation 에서 uncommitted_read 일 때, 1st tx, 2nd tx 중 2nd tx가 1번 field를
// 변경했는데 그 이후 1st 가 해당 entity를 읽어와서 2번 field를 변경하고 save했을 때,
// 2nd tx가 롤백하더라도 롤백이 적용되지 않고, 1st 가 2번 field를 변경했더라도, 2nd 가
// 변경한 1번 field 내용이 같이 커밋된다. 이 때 @DynamicUpdate를 쓰면 1st 가 save할 때
// 자신이 변경한 2번 field 만 update 쿼리로 db에 날라가게 된다.
// @DynamicUpdate
// SoftDelete를 위함 => book 엔티티에 대한 쿼리메서드에 항상 false 가 붙어서 실행됨.
@Where(clause = "deleted = false")
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

    // book 엔티티가 persist가 될 때, publisher 도 같이 persist 실행해라.
    @ManyToOne(cascade =
            { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
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

    private boolean deleted;


    // 아래는 코드로 이루어진 데이터.
    // 코드로 이루어진 데이터를 직접 활용하는 것은 orm 측면에서 좀 어긋났다고 볼 수 있다.
    // => 좀 더 의미가 있는 형태의 객체로 만들기 위해, BookStatus 라는 클래스를 만든다.
    /*
    private int status; // 판매 상태

    public boolean isDisplayed() {
        return status == 200;
    }
    */

    // autoApply 적용하면 아래 Converter 적용 안해줘도 된다.
    // 또한, autoApply는 IntegerConverter, StringConverter 이런 컨버터 만들어서
    // 적용하면 안된다. 꼭 개발자가 생성한 클래스에 한에서 활용해야 한다.
    // 그렇지 않으면, 모든 varchar 타입, 모든 number 타입의 컬럼들은 해당 컨버터를
    // 타게 된다. 그래서 이런 제네럴한 타입은 autoApply 끄고, Convert Annotation을
    // 각 field에 적용하여 필요한 field만 컨버터를 타도록 한다.
    // @Convert(converter = BookStatusConverter.class)
    private BookStatus status;


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
