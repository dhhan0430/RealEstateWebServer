package com.fastcampus.jpa.bookmanager2.domain;

import com.fastcampus.jpa.bookmanager2.domain.listener.Auditable;
import com.fastcampus.jpa.bookmanager2.domain.listener.UserEntityListener;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor // 원하는 멤버 변수에 @NonNull 을 적용해서 사용한다.
@Builder
@Data
// @Data = @Getter, @Setter, @ToString, @Value
//          @RequiredArgsConstructor, @EqualsAndHashCode
@Entity // Entity는 (PK)Primary Key 가 반드시 필요하다.
@Table(name = "user") // sql: create table user_legacy ...
/*
// 주의해야할 점: 해당 index나 constraint(제약사항)은 실제로 db에 적용된 것과 다를 수 있다는 것.
// jpa entity를 활용해서 ddl 을 생성하는 경우에는 실제로 적용이 되지만,
// 일반적으로 많이 사용하는 select/update/insert/delete 쿼리를 실행할 때는 아무런 효과를 주지
// 않는다. 즉, 실제 db에 index가 적용되어 있지 않은데, jpa에 index 설정이 있다고 해서,
// index를 활용한 쿼리가 동작하거나 그러진 않는다. 보편적으로는 이런 index나 제약사항은
// db에 설정에 맡기고 entity에 표기하지 않는 경우가 좀 더 많다.
@Table(name = "user", indexes = { @Index(columnList = "name") },
        uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) })
*/
@EntityListeners(value = { /*AuditingEntityListener.class,*/ UserEntityListener.class })
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    @Id       // User 라는 테이블 객체의 Id 값 (PK)
    // 기본키 생성을 데이터베이스에 위임해주고 id가 null이면 알아서 auto_increment 해준다.
    // h2 db의 default 값은 SEQUENCE 인데, IDENTITY 를 사용하게 되면,
    // call next value for hibernate_sequence; 명령어를 사용할 수 없다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IDENTITY: mysql, maria db에서 많이 사용하는 전략.
    // table 마다 해당 id 값을 auto increment 로 지정을 하여, 지속적으로 값을 증가시켜 준다.
    // mysql의 경우 db의 auto implementation 값을
    // 활용해서 이 generation을 제공하고 있다. 실제로 사용하게 되면 트랜잭션이 종료되기 전에
    // insert 문이 동작을 해서 id 값을 사전에 가져오게 된다. 실제로 commit되지 않고 로직이 종료된다고
    // 하더라도, db에서 가지고 있는 id 값을 증가시키고 있어서 마치 이빨 빠진 것처럼 특정 id 값이
    // 비는 현상이 발생하기도 한다.
    // SEQUENCE: sequence라는 특별한 함수를 제공하고 있는 oracle, postgresql 등에서 사용하게
    // 된다. h2 db도 기본적으로 sequence strategy 를 사용한다.
    // call next value for hibernate_sequence; => insert 구문이 실행이 될 때,
    // sequence 로부터 증가된 값을 받아서 실제 트랜잭션이 종료되는 시점에 insert 구문에
    // id를 채워서 쿼리를 하게 된다.
    // TABLE: db 종료에 상관 없이, id 값을 관리하는 별도의 테이블을 만들어 놓고, 그 테이블에서
    // id 값을 계속 추출해서 사용할 수 있도록 제공하고 있다.
    // AUTO: 각 db에 적합한 값을 자동으로 넘겨주게 된다. db 의존성 없이 사용할 수 있다.
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String email;
    // @Column(name = "crtdat") // db의 column과 object의 name을 별도로 매핑하기 위해 사용.
    // @Column(nullable = false) // ddl 쿼리를 할 때 not null 필드를 자동으로 만들어줌.

    // string으로 하지 않으면, default가 ordinary라서 숫자로 저장된다.
    // 예를 들어 MALE이 0이기 때문에 DB에 숫자 0으로 저장된다. 근데 그 이후에 리팩토링으로 인해
    // 새로운 ENUM이 0으로 들어오게 되면, DB에 있는 값과 매칭이 되지 않는다.
    // STRING으로 하면, DB에도 STRING 형태로 저장된다.
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    /*
    private String city;
    private String district;
    private String detail;
    private String zipCode;
    */

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "district", column = @Column(name = "home_district")),
        @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code"))

    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "company_city")),
        @AttributeOverride(name = "district", column = @Column(name = "company_district")),
        @AttributeOverride(name = "detail", column = @Column(name = "company_address_detail")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "company_zip_code"))

    })
    private Address companyAddress;


    // 여기 @OneToMany 에서는 참조되는 값을 One에서 가지고 있지 않다.
    // One 에 해당 하는 PK 를 Many 쪽에서 FK 로 가지고 있게 된다.
    // 즉 여기선, UserHistory 테이블에서 User의 id 값을 FK 로 가지고 있게 된다.
    @OneToMany(fetch = FetchType.EAGER) // LazyInitializationException 에러 제거.
    // entity가 어떤 column으로 join을 하게 될지 지정해준다.
    // JoinColumn을 넣지 않았을 때는 User와 UserHistory 매핑 테이블이 자동 생성된다.
    // JoinColumn을 넣으면 매핑 테이블이 사라지고, UserHistory 테이블에 UserHistoryId
    // field가 생성되게 된다.
    // JoinColumn에서 default 값이 해당 field의 name을 활용해서 만들어 준다.
    // insertable: false, updatable: false 은 User Entity에서 UserHistory를
    // 저장하거나 수정하지 못하게 해준다.
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // getUserHistories 했을 때 null 포인터 반환되지 않도록 new를 통해 기본 리스트 생성해줌.
    // 사실 jpa 에서는 해당 값을 조회할 때 값이 존재하지 않으면, 빈 list를 자동으로 넣어준다.
    // 그래서 일반적으로 문제는 없지만, jpa에서 persist를 하기 전에 해당 값이 null 이기 때문에
    // 로직에 따라서는 null pointer exception 이 발생할 수도 있다.
    // 그래서 아래와 같이 기본 생성자를 넣어주는 것이 좋다.
    @ToString.Exclude
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany // (fetch = FetchType.EAGER) : 이거 적용해도 에러난다.
    @JoinColumn(name = "user_id") // OneToMany는 매핑 테이블 생성된다.
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();


    /*
    @Column(updatable = false) // update할 때 해당 필드 반영 안함
    @CreatedDate
    private LocalDateTime createdAt;

    // @Column(insertable = false) // insert할 때 해당 필드 반영 안함
    @LastModifiedDate
    private LocalDateTime updatedAt;
    */

    /*
    영속성 처리에서 제외되기 때문에 db 데이터에 반영되지 않고 해당 객체와 생성 주기를 같이 하게 됨.
    @Transient
    private String testData;
    */

    /*
    @OneToMany(fetch = FetchType.EAGER)
    private List<Address> address;
    */

    // Event Listener
    // jpa에서 제공하는 이벤트는 7가지가 있다.
    // @PrePersist // insert(persist) 가 호출되기 전에 실행되는 메서드
    // @PreUpdate // merge 가 호출되기 전에 실행되는 메서드
    // @PreRemove // delete 가 호출되기 전에 실행되는 메서드
    // @PostPersist
    // @PostUpdate
    // @PostRemove
    // @PostLoad // select 조회가 일어난 직후에 실행되는 메서드

    // 이걸 이렇게 함수를 만들어서 적용하면, 매 entity마다 이렇게 반복적인 함수를 생성해주어야 한다.
    // => entity listener 를 생성하여 모든 entity에 일괄적으로 적용한다.
    /*
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    */
    /*
    @PostPersist
    public void postPersist() {
        System.out.println(">>> postPersist");
    }
    */
    /*
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    */

    /*
    @PostUpdate
    public void postUpdate() {
        System.out.println(">>> postUpdate");
    }

    @PreRemove
    public void preRemove() {
        System.out.println(">>> preRemove");
    }

    @PostRemove
    public void postRemove() {
        System.out.println(">>> postRemove");
    }

    @PostLoad
    public void postLoad() {
        System.out.println(">>> postLoad");
    }
    */
}
