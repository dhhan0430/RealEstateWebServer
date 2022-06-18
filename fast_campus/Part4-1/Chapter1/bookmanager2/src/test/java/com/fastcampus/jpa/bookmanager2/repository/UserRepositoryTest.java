package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.Address;
import com.fastcampus.jpa.bookmanager2.domain.Gender;
import com.fastcampus.jpa.bookmanager2.domain.User;
import com.fastcampus.jpa.bookmanager2.domain.UserHistory;
import org.apache.tomcat.jni.Local;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest
// 각 테스트 메서드가 종료할 때마다, 처리했던 데이터들을 모두 롤백해준다.
// 예를 들어 test directory에 있는 모든 테스트 메서드들을 실행하면, 현재 사용하고 있는 db에
// 데이터들이 공유되어서 서로의 테스트 메서드의 결과에 영향을 준다.
// 이렇게 Transactional 을 적용하면 서로 영향을 주지 않는다.
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    // @Transactional
    void crud() {
        // userRepository.save(new User());
        // userRepository.findAll().forEach(System.out::println);

        // DESC : 역순
        /*
        List<User> users = userRepository
                .findAll(Sort.by(Sort.Direction.DESC, "name"));
        */

        /*
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(3L);
        ids.add(5L);
        List<User> users = userRepository.findAllById(ids);
        */

        /*
        List<User> users = userRepository
                .findAllById(Lists.newArrayList(1L, 3L, 5L));

        users.forEach(System.out::println);
        */

        /*
        User user1 = new User("jack", "jack@fastcampus.com");
        User user2 = new User("steve", "steve@fastcampus.com");
        userRepository.saveAll(Lists.newArrayList(user1, user2));

        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
        */

        /*
        User user = userRepository.getOne(1L);
        // 그냥 여기서 print 해버리면 no session 에러 발생한다.
        // 위에 @Transactional 붙여줘야 세션이 유지된다.
        System.out.println(user);
        */

        /*
        // Optional<User> user = userRepository.findById(1L);
        User user = userRepository.findById(1L).orElse(null);
        System.out.println(user);
        */

        /*
        userRepository.save(new User("new martin",
                "newmartin@fastcampus.com"));
        userRepository.flush();
        userRepository.findAll().forEach(System.out::println);

        long count = userRepository.count();
        System.out.println(count);

        boolean exists = userRepository.existsById(1L);
        System.out.println(exists);
        */

        /*
        userRepository.delete(userRepository
                .findById(1L).orElseThrow(RuntimeException::new));
        */

        /*
        userRepository.deleteById(1L);
        userRepository.findAll().forEach(System.out::println);
        */

        /*
        userRepository.deleteAll();
        */

        /*
        // deleteInBatch 를 사용하면, for 문을 돌면서 1개씩 삭제하는 deleteAll() 보다
        // 훨씬 성능이 빠르다. for 문을 도는 것이 아니라 한 번에 삭제하기 때문이다.
        userRepository.deleteInBatch(
                userRepository.findAllById(Lists.newArrayList(1L, 3L))
        );
        */

        /*
        // page index는 0부터 시작한다. page 1을 요청한다는 것은 2번째 page를 요청한 것.
        Page<User> users = userRepository.findAll(PageRequest.of(1, 3));
        System.out.println("page: " + users);
        System.out.println("totalElements: " + users.getTotalElements());
        // page 사이즈로 전체 elements 를 나누면 몇 페이지까지 있는지 나온다.
        // 위에서 page 1개 사이즈를 3으로 했으니 총 페이지 수는 2 다.
        System.out.println("totalPages: " + users.getTotalPages());
        System.out.println("numberOfElements: " + users.getNumberOfElements());
        System.out.println("sort: " + users.getSort());
        System.out.println("size: " + users.getSize());

        users.getContent().forEach(System.out::println);
        */

        /*
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", endsWith());

        // 여기 Example.of() 들어가는 entity는 probe라고 하는데 실제 객체가 아닌 가짜 entity
        Example<User> example =
                Example.of(new User("ma", "fastcampus.com"), matcher);

        userRepository.findAll(example).forEach(System.out::println);
        */


        userRepository.save(new User("david", "david@fastcampus.com"));
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setEmail("martin-updated@fastcampus.com");

        // 이 메서드는 update인데 update 쿼리는 delete와 마찬가지로 해당 entity가 존재하는지
        // 확인하기 위해 select를 먼저 실행한 후, update 한다.
        // save를 하면 일단 select 쿼리가 동작하고, 원래 해당 pk의 entity가 존재했다면,
        // update 쿼리를 실행한다.
        // save는 경우에 따라 insert 쿼리 / update 쿼리 로 동작한다.
        // SimpleJpaRepository 클래스의 save 메서드 내용을 보면,
        // 해당 entity가 isNew이면, em.persist() 즉, insert가 동작하고,
        // isNew가 아니라면, em.merge() 즉, update가 동작한다.
        userRepository.save(user);


    }

    @Test
    void select() {
        /*
        System.out.println(userRepository.findByName("martin"));
        // 아래 쿼리들은 모두 동일한 결과를 리턴해준다.
        System.out.println("findByEmail: " + userRepository.findByEmail("martin@fastcampus.com"));
        System.out.println("getByEmail: " + userRepository.getByEmail("martin@fastcampus.com"));
        System.out.println("readByEmail: " + userRepository.readByEmail("martin@fastcampus.com"));
        System.out.println("queryByEmail: " + userRepository.queryByEmail("martin@fastcampus.com"));
        System.out.println("searchByEmail: " + userRepository.searchByEmail("martin@fastcampus.com"));
        System.out.println("streamByEmail: " + userRepository.streamByEmail("martin@fastcampus.com"));
        System.out.println("findUserByEmail: " + userRepository.findUserByEmail("martin@fastcampus.com"));
        System.out.println("findTop1ByName: " + userRepository.findTop1ByName("martin"));
        System.out.println("findFirst1ByName: " + userRepository.findFirst1ByName("martin"));
        */

        /*
        System.out.println("findByEmailAndName: " + userRepository.findByEmailAndName("martin@fastcampus.com", "martin"));
        System.out.println("findByEmailOrName: " + userRepository.findByEmailOrName("martin@fastcampus.com", "dennis"));
        System.out.println("findByCreatedAtAfter: " + userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByIdAfter: " + userRepository.findByIdAfter(4L));
        System.out.println("findByCreatedAfterGreaterThan: " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAfterGreaterThanEqual: " + userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));

        System.out.println("findByCreatedAtBetween: " + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween: " + userRepository.findByIdBetween(1L, 3L));
        System.out.println("findByIdGreaterThanEqualAndIdLessThanEqual: " + userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1L, 3L));

        System.out.println("findByIdIsNotNull: " + userRepository.findByIdIsNotNull());
        // Empty 관련 메서드는 collection properties 에서만 사용 가능.
        // System.out.println("findByAddressIsNotEmpty: " + userRepository.findByAddressIsNotEmpty());

        System.out.println("findByNameIn: " + userRepository.findByNameIn(Lists.newArrayList("martin", "dennis")));

        System.out.println("findByNameStartingWith: " + userRepository.findByNameStartingWith("mar"));
        System.out.println("findByNameEndingWith: " + userRepository.findByNameEndingWith("tin"));
        System.out.println("findByNameContains: " + userRepository.findByNameContains("art"));

        System.out.println("findByNameLike: " + userRepository.findByNameLike("%art%"));
        */
    }

    @Test
    void pagingAndSortingTest() {
        System.out.println("findTop1ByName: " + userRepository.findTop1ByName("martin"));
        System.out.println("findTop1ByNameOrderByIdDesc: " + userRepository.findTop1ByNameOrderByIdDesc("martin"));
        System.out.println("findFirstByNameOrderByIdDescEmailAsc: " + userRepository.findFirstByNameOrderByIdDescEmailAsc("martin"));
        System.out.println("findFirstByNameWithSortParams: " + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));

        // 먼저 name 기준으로 entity 들을 찾아 놓고, 그 결과들을 가지고 페이징 작업을 해서 출력해준다.
        System.out.println("findByNameWithPaging: " + userRepository.findByName("martin", PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")))).getContent());
    }

    @Test
    void insertAndUpdateTest() {
        User user = new User();
        user.setName("martin");
        user.setEmail("martin2@fastcampus.com");
        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("marrrrrtin");
        userRepository.save(user2);
    }

    @Test
    void enumTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);
        userRepository.save(user);
        userRepository.findAll().forEach(System.out::println);

        System.out.println(userRepository.findRawRecord().get("gender"));
    }

    @Test
    void listenerTest() {
        User user = new User();
        user.setEmail("martin2@fastcampus.com");
        user.setName("martin");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("marrrrrtin");

        userRepository.save(user2);

        userRepository.deleteById(4L);
    }

    @Test
    void prePersistTest() {
        User user = new User();
        user.setEmail("martin2@fastcampus.com");
        user.setName("martin");

        userRepository.save(user);
        System.out.println(userRepository.findByEmail("martin2@fastcampus.com"));
    }

    @Test
    void preUpdateTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        System.out.println("as-is : " + user);

        user.setName("martin22");
        userRepository.save(user);

        System.out.println("to-be : " + userRepository.findAll().get(0));
    }

    @Test
    void userHistoryTest() {
        User user = new User();
        user.setEmail("martin-new@fastcampus.com");
        user.setName("martin-new");

        userRepository.save(user);

        user.setName("martin-new-new");

        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);

    }

    @Test
    void userRelationTest() {

        User user = new User();
        user.setName("david");
        user.setEmail("david@fastcampus.com");
        user.setGender(Gender.MALE);
        userRepository.save(user);

        user.setName("daniel");
        userRepository.save(user);

        user.setEmail("daniel@fastcampus.com");
        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
        /*
        List<UserHistory> result = userHistoryRepository.findByUserId(
            userRepository.findByEmail("daniel@fastcampus.com").getId()
        );
        */

        List<UserHistory> result =
                userRepository.findByEmail("daniel@fastcampus.com")
                                .getUserHistories();

        System.out.println("----------------------------------------");
        result.forEach(System.out::println);

        System.out.println("----------------------------------------");
        System.out.println("UserHistory.getUser(): "
                + userHistoryRepository.findAll().get(0).getUser());
    }


    @Test
    void embedTest() {
        userRepository.findAll().forEach(System.out::println);

        User user = new User();
        user.setName("steve");
        user.setHomeAddress(
                new Address(
                        "서울시",
                        "강남구",
                        "강남대로 364 마왕빌딩",
                        "06241"
                )
        );
        user.setCompanyAddress(
                new Address(
                        "서울시",
                        "성동구",
                        "성수이로 113 제강빌딩",
                        "04794"
                )
        );
        userRepository.save(user);

        User user1 = new User();
        user1.setName("joshua");
        user1.setHomeAddress(null);
        user1.setCompanyAddress(null);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("jordan");
        user2.setHomeAddress(new Address());
        user2.setCompanyAddress(new Address());
        userRepository.save(user2);

        // 여기서 영속성 컨텍스트의 캐시를 clear 해주면, joshua와 jordan은 캐시에서
        // 가져오는 것이 아니라, db에서 다시 조회를 해서 데이터를 처리하게 된다.
        entityManager.clear();

        userRepository.findAll().forEach(System.out::println);
        userHistoryRepository.findAll().forEach(System.out::println);

        userRepository.findAllRawRecode().forEach(
                a -> System.out.println(a.values())
        );

    }
}