package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.Book;
import com.fastcampus.jpa.bookmanager2.repository.dto.BookNameAndCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookRepository extends JpaRepository<Book, Long> {

    // update() 메서드를 실행하면 @Query 내의 sql 문이 실행이 된다.
    @Modifying
    @Query(value = "update book set category='none'", nativeQuery = true)
    void update();

    List<Book> findByCategoryIsNull();
    List<Book> findAllByDeletedFalse();
    List<Book> findByCategoryIsNullAndDeletedFalse();

    // 쿼리 메서드 단점: 이렇게 메서드 만들면 가독성 어려움.
    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual
    (String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    // jpql 문법 => dialect를 통해 해당 db에 맞는 쿼리를 자동으로 생성해준다.
    /* 방법 1
    @Query(value = "select b from Book b "
            + "where name = ?1 and createdAt >= ?2 "
            + "and updatedAt >= ?3 and category is null")
    List<Book> findByNameRecently
            (String name, LocalDateTime createdAt, LocalDateTime updatedAt);
    */

    // 방법 2
    @Query(value = "select b from Book b "
            + "where name = :name and createdAt >= :createdAt "
            + "and updatedAt >= :updatedAt and category is null")
    List<Book> findByNameRecently(
            @Param("name") String name,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("updatedAt") LocalDateTime updatedAt);


    /* Tuple or Interface로 가져오는 방법
    @Query(value = "select b.name as name, b.category as category from Book b")
    //List<Tuple> findBookNameAndCategory();
    // tuple이 아니라 interface로 가져올 수 있다.
    List<BookNameAndCategory> findBookNameAndCategory();
    */

    // => Query annotation을 활용해서 쿼리 메서드의 이름을 좀 더 가독성 있게 작성할 수 있고,
    // entity 전체가 아닌 필요한 컬럼에서 부분적으로 몇 개만 추출을 해서 조회할 수 있다.
    // 추출할 때는 interface 형식과 class 형식으로 추출할 수 있다.
    // 또한 paging 기능도 쉽게 추가할 수 있다.
    @Query(value =
            "select new com.fastcampus.jpa.bookmanager2.repository.dto.BookNameAndCategory" +
                    "(b.name, b.category) from Book b"
    )
    List<BookNameAndCategory> findBookNameAndCategory();

    @Query(value =
            "select new com.fastcampus.jpa.bookmanager2.repository.dto.BookNameAndCategory" +
                    "(b.name, b.category) from Book b"
    )
    Page<BookNameAndCategory> findBookNameAndCategory(Pageable pageable);

    // Native Query
    // Native Query는 jpql과 다르게 entity 속성을 사용할 수 없다.
    // 아래 쿼리문에서 book은 Entity가 아니라 table 이름이다.
    // Native Query는 일반 쿼리 메서드와는 다르게 entity 클래스에 선언한 Where annotation이
    // 적용되지 않는다.
    @Query(value = "select * from book", nativeQuery = true)
    List<Book> findAllCustom();

    // 일반적으로 dml(update, delete) 이런 작업들에서는 리턴되는 값이 적용된 row 개수로 표시됨.
    @Modifying // update
    @Transactional
    @Query(value = "update book set category = 'IT전문서'", nativeQuery = true)
    int updateCategories();

    // Native Query는 "show tables" 같이 일반 쿼리메서드로 제공되지 않은 쿼리를
    // 써야할 때도 사용한다.

    @Query(value = "show tables", nativeQuery = true)
    List<String> showTables();

    // id 역순으로 1개만 select
    @Query(value = "select * from book order by id desc limit 1", nativeQuery = true)
    Map<String, Object> findRawRecord();

}
