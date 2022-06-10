package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository Interface 를 상속 받음으로 인해, 많은 JPA 관련 메서드들을 사용 가능.
// User: jpa Entity, Long: PK
public interface UserRepository extends JpaRepository<User, Long> {


}
