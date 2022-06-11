package com.fastcampus.jpa.bookmanager2.service;

import com.fastcampus.jpa.bookmanager2.domain.User;
import com.fastcampus.jpa.bookmanager2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class UserService {

    @Autowired
    private EntityManager entityManager;

    // UserRepository를 Autowired로 가져와서 save 실행시키면, save 메서드 내부에
    // EntityManager를 이용하여 영속화를 시킬 수 있다.
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void put() {
        User user = new User();
        user.setName("newUser");
        user.setEmail("newUser@fastcampus.com");

        /*
        // 트랜잭션 끝날 때 커밋될 때 flush 된다.?
        // save 메서드 안에서도 persist 메서드가 실행된다.
        // persist 메서드를 실행을 하게 되면, 영속성 컨텍스트가 해당 entity를 관리하는 상태,
        // 즉, managed 상태가 된다.
        // persist를 했다는 것은 해당 entity 가 db에 잘 영속화 되도록 해준다.
        entityManager.persist(user);

        // 여기서 중요한 점은 아래와 같이 setName을 해주고 persist를 해주지 않더라도,
        // transaction이 끝나면, 자동으로 해당 내용이 DB로 flush 된다.
        // 이는 해당 객체가 영속성 컨텍스트에서 관리되고 있기 때문이다.
        // 이는 마치 DB 캐시에 있는 데이터를 변경해준 것이 자동으로 DISK에 반영되는 것과 비슷하다.\
        // 이처럼 save를 하지 않아도 해당 내용이 db에 update되는 쿼리가 발생하는 이유는
        // 영속성 컨텍스트가 제공해주는 dirty check 라는 기능 때문이다.
        // 영속성 컨텍스트가 가지고 있는 entity 객체는 처음 컨텍스트에 로드할 때,
        // 해당 정보를 일종의 스냅샷으로 가지고 있다. 그런 후에 변경 내용을 db에 적용하는 시점
        // 3가지 상황 중 하나가 오면, 스냅샷과 현재 entity 값을 비교해서 변경된 내용이 있다면,
        // persist 메서드가 없다 하더라도 자동으로 db에 영속화시켜준다.
        // 따라서, 이럴 때 대량의 entity를 다루게 될 경우에는 성능 저하가 발생하기도 한다.
        // 영속성 컨텍스트 내에서 관리되는 entity는 setter를 통해
        // enttiy 정보가 변경이 될 경우에 트랜잭션이 완료될 시점에 별도에 save 메서드를
        // 호출하지 않더라도 DB 데이터의 정합성을 맞춰주게 된다.
        user.setName("newUserAfterPersist");
        */

        entityManager.persist(user);
        // detach를 실행하면 detached 상태로 빠지게 되며, 아래에서 setName을 하더라도,
        // managed 상태가 아니기 때문에 DB에 반영되지 않는다.
        entityManager.detach(user);

        user.setName("newUserAfterPersist");
        // detached 상태에 있는 entity를 다시 merge 메서드를 하게 되면,
        // 다시 managed 상태로 빠지게 된다.
        // persist, merge 메서드 모두 save 메서드에 포함되어 있다.
        entityManager.merge(user);

        // clear 메서드는 detach 처럼 detached 상태로 만들어주는데, detach보다
        // 좀 더 파괴적인 방법이다. 컨텍스트에서 변경 예약되어 있었던 부분들까지 모두
        // 삭제시켜준다.
        // 따라서, clear를 호출하게 될 상황이 오면 clear() 호출 전에,
        // flush()를 호출하면 된다.
        //entityManager.clear();

        // removed 상태가 되면 해당 entity는 더이상 사용하지 못하게 되며,
        // remove 메서드는 해당 entity가 managed 상태일 때 적용 가능하다.
        // entityManager.remove(user);

        User user1 = userRepository.findById(1L).get();
        entityManager.remove(user1);

        // 위에서 제거했기 때문에 당연히 에러 발생한다.
//        user1.setName("new_martin");
//        entityManager.merge(user1);

    }


}
