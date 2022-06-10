package com.fastcampus.jpa.bookmanager2.service;

import com.fastcampus.jpa.bookmanager2.domain.User;
import com.fastcampus.jpa.bookmanager2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void entityManagerTest() {
        System.out.println(
                // User 라는 Entity를 u라고 표시했을 때 u를 모두 가져와라.
                // userRepository.findAll(); 과 같다.
                entityManager.createQuery("select u from User u")
                        .getResultList()
        );
    }

    @Test
    void cacheFindTest() {

//        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));
//        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));
//        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));

        // 위에 Transactional annotation을 달아서 1개의 트랜잭션로 실행시키게 되면,
        // 1번의 select 쿼리가 실행된다.
        // 따로 캐시 설정을 하진 않았지만, 영속성 컨텍스트 내에서 자동으로 entity에서 캐시 처리
        // 하는 것을 일반적으로 jpa의 1차 캐시라고 한다.
        // 1차 캐시는 map(key, value)의 형태로 만들어진다.
        // key: id, value: entity
        // id 로 조회하게 되면 영속성 컨텍스트 내에 존재하는 1차 캐시에 entity가 있는지 확인해
        // 보고 있으면 db 조회없이 바로 값을 리턴해주고, 만약에 1차 캐시 내에 존재하지 않는다면,
        // 실제 쿼리로 db 로부터 값을 캐시에 저장하고 리턴해준다.
        // 1차 캐시가 동작을 해야 기본적인 jpa 조회 성능이 올라가게 된다.
//        System.out.println(userRepository.findById(1L).get());
//        System.out.println(userRepository.findById(1L).get());
//        System.out.println(userRepository.findById(1L).get());

        // 하나의 transaction 안에서 동작할 때는 1차 캐시를 사용함으로써 성능 저하를 방지하도록
        // 되어 있다.
        // @Transactional 이 적용된 메서드는 하나의 트랜잭션으로 묶인다.
        userRepository.deleteById(1L);

    }

    @Test
    void cacheFindTest2() {

        User user = userRepository.findById(1L).get();
        user.setName("marrrrrtin");

        userRepository.save(user);

        System.out.println("-------------------------------");

        user.setEmail("marrrrrtin@fastcampus.com");

        userRepository.save(user);

        // @Transactional 시, 아래와 같이 flush()를 해주면,
        // 위에서 save 한 것들의 정보들을 가지고 있다가 여기서 한 번에 flush 해준다.
        // entity cache 쪽에서 2개의 save 쿼리를 merging 한 후,
        // 한 번에 업데이트해준다.
        // flush를 안 해주면 롤백된다. 롤백되는 이유는 2번의 save가 있었지만,
        // 하나의 Transaction으로 묶인 상태이고, 이 메서드 자체가 @Test 이기
        // 때문에 롤백되는 것이다.
        userRepository.flush();

    }
}
