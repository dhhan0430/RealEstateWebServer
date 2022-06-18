package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {

    // JpaRepository 인터페이스를 상속받은 인터페이스를 통해 간단한 쿼리 메서드 생성하기.

    // 이름을 통해 User entity를 가져오는 메서드.
    // 선언만 해도 사용 가능.

    // 리턴 타입은 List<User>, Optional<User> 등 가능하다. (개발 스펙 문서 참조)
    // Spring Data JPA 에서 Wrapping 해서 리턴해주게 된다.
    List<User> findByName(String name);

    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    User findUserByEmail(String email);

    // 1이면 1개 리턴, 2면 2개 리턴
    // 참고로 Last1 이런 메서드는 없다. OrderBy 로 역순으로 가져온 다음에 First1 호출해야 한다.
    List<User> findFirst1ByName(String name);
    // List<User> findTop1ByName(String name);

    /*
    List<User> findByEmailAndName(String email, String name);
    List<User> findByEmailOrName(String email, String name);
    List<User> findByCreatedAtAfter(LocalDateTime yesterday);
    List<User> findByIdAfter(Long id);
    List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday);
    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);
    List<User> findByCreatedAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);
    List<User> findByIdBetween(Long id1, Long id2);
    List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);

    List<User> findByIdIsNotNull();
    // Empty 관련 메서드는 collection properties 에서만 사용 가능.
    // List<User> findByAddressIsNotEmpty();

    List<User> findByNameIn(List<String> names);

    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByNameContains(String name);
    List<User> findByNameLike(String name);

    // findByName() 과 같다.
    List<User> findByNameIs(String name);
    */

    List<User> findTop1ByName(String name);

    List<User> findTop1ByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDescEmailAsc(String name);

    List<User> findFirstByName(String name, Sort sort);

    // Pageable: paging에 대한 요청값, Page: paging에 대한 응답값
    Page<User> findByName(String name, Pageable pageable);

    @Query(value = "select * from user limit 1;", nativeQuery = true)
    Map<String, Object> findRawRecord();

    @Query(value = "select * from user", nativeQuery = true)
    List<Map<String, Object>> findAllRawRecode();

}
