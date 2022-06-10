package com.fastcampus.jpa.bookmanager2.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor // 엔티티는 기본적으로 아무 파라미터도 받지 않는 NoArgsConstructor가 필요.
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookReviewInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long bookId;
    // @OneToOne // 1대1로 연관관계를 맺는다는 뜻.
    // 테이블에는 bookId 라는 Long 값이 실제로 존재하겠지만, JPA에서는 Entity로 set, get을
    // 하게 되면, relation을 자동으로 맺을 수 있도록 처리를 해준다.
    // optional = false: book 은 절대 null 을 허용하지 않는다.
    // mappedBy = "bookReviewInfo": 해당 key(book)를  bookReviewInfo 테이블에서
    // 더 이상 가지지 않게 된다. book <-> bookReviewInfo 관계에서 주인이 book.
    // 아래와 같이 mappedBy 를 걸면, bookReviewInfo에는 분명히 book을 넣어서
    // save 해주었지만, select로 bookReviewInfo를 가져오게 되면, book field가
    // null로 되어있다. 즉, mappedBy로 인해 book에 대한 FK 가 없어진 것이다.
    // 하인이 주인을 세팅을 한 것이 의미가 없다.
    // 주인이 하인을 세팅해줘야 서로 참조가 가능하다. (주인이 하인만 세팅해주면 된다.)
    // @OneToOne(optional = false, mappedBy = "bookReviewInfo")
    @OneToOne(optional = false)
    private Book book; // 이 매핑으로 인해 book에 대한 select 쿼리가 한 번 더 발생.

    private float averageReviewScore;

    private int reviewCount;
}
