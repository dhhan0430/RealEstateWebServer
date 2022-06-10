package com.fastcampus.jpa.bookmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor // 원하는 멤버 변수에 @NonNull 을 적용해서 사용한다.
@Builder
@Data
// @Data = @Getter, @Setter, @ToString, @Value
//          @RequiredArgsConstructor, @EqualsAndHashCode
@Entity // Entity는 (PK)Primary Key 가 반드시 필요하다.
@Table(name = "users")
public class User {
    @Id       // User 라는 테이블 객체의 Id 값 (PK)
    // 기본키 생성을 데이터베이스에 위임해주고 id가 null이면 알아서 auto_increment 해준다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
