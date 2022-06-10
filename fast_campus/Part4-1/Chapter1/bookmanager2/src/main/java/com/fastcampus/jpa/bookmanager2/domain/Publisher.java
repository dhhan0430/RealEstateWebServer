package com.fastcampus.jpa.bookmanager2.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Publisher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    // 현재 publisher (One) <-> book (Many) 관계이다.
    // 보통 인터넷 자료에서는 Book entity (주인) 쪽에서 @JoinColumn(name = "publisher_id")로
    // 세팅을 하는데, 여기에 해도 똑같이 Book 테이블에 publisher FK 가 적용되는 것을 보니,
    // 둘 중 아무데나 @JoinColumn(name = "publisher_id") 을 적용하더라도,
    // 항상 jpa에서는 주인을 Many로 잡고 Many 쪽 테이블에 FK 를 적용시키는 것으로 보인다.
    // 그런데 다시 보니, @JoinColumn을 적용하지 않더라도 항상 Many인 Book 테이블에는
    // publisher_id 값이 들어간다. @JoinColumn을 적용하지 않으면 매핑 테이블이 생긴다.
    @JoinColumn(name = "publisher_id")
    private List<Book> books = new ArrayList<>();

}
