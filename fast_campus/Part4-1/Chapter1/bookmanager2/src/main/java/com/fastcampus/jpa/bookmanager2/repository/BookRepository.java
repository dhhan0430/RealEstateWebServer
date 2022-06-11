package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    // update() 메서드를 실행하면 @Query 내의 sql 문이 실행이 된다.
    @Modifying
    @Query(value = "update book set category='none'", nativeQuery = true)
    void update();
}
