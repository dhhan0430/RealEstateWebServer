package com.fastcampus.jpa.bookmanager2.service;

import com.fastcampus.jpa.bookmanager2.domain.Author;
import com.fastcampus.jpa.bookmanager2.domain.Book;
import com.fastcampus.jpa.bookmanager2.repository.AuthorRepository;
import com.fastcampus.jpa.bookmanager2.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    /*
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    */

    // final 이기 때문에 반드시 생성자가 필요하다.
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final EntityManager entityManager;
    private final AuthorService authorService;

    /*
    @RequiredArgsConstructor 로 인해 아래와 같이 생성자가 생성된다.
    위 field 가 final 이기 때문에 RequiredArgs 에 포함된다.
    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }
    */

    /*
    public void put() {

        this.putBookAndAuthor();
    }
    */

    @Transactional
    public void putBookAndAuthor() {

        Book book = new Book();
        book.setName("JPA 시작하기");

        // 여기서 save를 하면 perist 메서드가 동작해서 일단 sql 쿼리는 날리긴 한다.
        // 하지만 transactional 이 걸려있기 때문에 아직 commit 되진 않아서
        // 다른 session으로 db에 접속하여 select * from book; 하면 조회되지 않는다.
        // 대신 persist를 했으니 managed 상태가 됐을 것이고, 캐시에 존재할 것이다.
        bookRepository.save(book);

        Author author = new Author();
        author.setName("martin");

        authorRepository.save(author);

        throw new RuntimeException("오류가 나서 DB commit이 발생하지 않습니다");

    }

    // propagation test
    @Transactional(propagation = Propagation.REQUIRED)
    public void putBookAndAuthor2() {

        Book book = new Book();
        book.setName("JPA 시작하기");
        bookRepository.save(book);

        try {
            // Propagation.REQUIRED 일 때 아래 메서드에서 RuntimeException이
            // 발생하면 catch로 잡아서 후처리 한다고 하더라도 일단 RuntimeException이
            // 발생했다는 측면에서, 이미 발생하면 발생한 것이다. 그래서 같은 tx 내의
            // 작업들이 롤백된다.
            authorService.putAuthor();
        } catch (RuntimeException e) {

        }

        throw new RuntimeException("오류가 발생하였습니다. transaction은 어떻게 될까요?");
    }

    // READ_UNCOMMITTED 는 다른 tx의 커밋되지 않은 변경 값도 읽어오기 때문에
    // 정합성 보장은 거의 불가능이다.
    //@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    // @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    public void get(Long id) {
        System.out.println(">>> " + bookRepository.findById(id));
        System.out.println(">>> " + bookRepository.findAll());

        // READ_COMMITTED 일 때,
        // 지금 이 사이에서 2nd tx(터미널로 mysql 따로 실행) 가 field 변경 후 commit 해도
        // 아래 find 메서드들에서 출력되는 값은 2nd tx의 변경된 값이 아니다.
        // 이는 entity 캐시에 있는 값을 그대로 읽어와서 그렇다.
        // 아마 transactional이 끝나지 않은 상황이어서 entity 캐시 값이 그대로 유지된 것 같다.
        // 여기서 한 번 entityManager.clear(); 해주면 캐시가 클리어되기 때문에
        // db로부터 2nd tx의 변경되고 커밋된 값을 가져오게 된다.

        // entityManager.clear();

        // REPEATABLE_READ 의 정합성 약점인 Phantom read 현상 재현을 위함
        entityManager.clear();

        // 이렇게 변경된 값을 가져오는 상황을 UNREPEATABLE_READ 라고 한다.
        // 나는 그냥 하나의 tx 안에서 똑같은 entity를 조회했을 뿐인데 다른 값이 읽어지는 상황.
        // 나는 그냥 나의 tx 안에서 동일한 entity 값을 읽어오고 싶은데, READ_COMMITTED
        // 옵션이 켜져 있다보니, 중간에 값이 변경되어 커밋되면(+ 내 로직에서의 clear()도 포함),
        // 내 tx 안에서 entity 값이 변경되는 것이다.
        // 이런 상황을 해결하기 위한 것이 REPEATABLE_READ 다.
        // READ_COMMITTED -> REPEATABLE_READ
        // REPEATABLE_READ 는 tx 내에서 반복해서 조회하더라도 항상 같은 값이 읽혀진다.
        // READ_COMMITTED 일 때 2nd tx에서 변경된 값이 반영이 안 될 때,
        // 그리고 그런 이유는 entity 영속성 캐시의 영향으로 인한 것이고, transactional
        // 때문에 tx 간 isolation 때문에 반영이 안 된 것이 아니라,
        // 단지 entity 캐시의 값을 가져왔기 때문이다. clear() 작업 해주면, 2nd tx의 변경되고
        // 커밋된 값을 읽어올 수 있다.
        // REPEATABLE_READ는 아예 tx 레이어에서 2nd tx의 변경되고 커밋된 값을 차단시킨다.
        // tx isolation을 더 강화시킨 것이다. 자기 tx가 시작될 때의 조회했던 값을 스냅샷 찍어서
        // 갖고 있다가 자기 tx가 끝나기 전까지는 그 스냅샷 값을 계속 사용한다.

        System.out.println(">>> " + bookRepository.findById(id));
        System.out.println(">>> " + bookRepository.findAll());

        // REPEATABLE_READ 의 정합성 약점인 Phantom read 현상 재현을 위함.
        // 2nd tx에서 insert into book (`id`, `name`) values (2, 'jpa 강의2'); 를
        // 한 상황에서 여기서 update(): update book set category='none'; 를 하게 되면,
        // 당연히 REPEATABLE_READ 로 인해 id 1의 값만 none 으로 바뀔 것 같은데,
        // 2nd tx에서 insert한 id 2의 값도 none으로 바뀌게 된다.
        // 이렇게 데이터(2nd tx의 데이터)가 안 보이는데 처리가 되는 경우를 phantom read 라고 한다.
        // 왜 이런 일이 발생하는지 생각해보면,
        // 일단 여기서 update 쿼리를 날렸을 때, 2nd tx의 id 2 값도 변경된 것을 보면,
        // 그리고 여기서 update():write 를 실행했을 때만 해도, 2nd tx는 커밋되지 않았었다.
        // 2nd tx가 커밋이 되고, lock이 풀리면서 update():write가 해당 table의 entity들에
        // 대해 update가 실행될 때 2nd tx의 id 2에도 적용이 되었다.
        // 이는 아무래도 REPEATABLE_READ가 read에 대해서만 정합성을 보존해주기 때문이 아닌가 싶다.
        // 이를 방지하는, 아예 write 조차도 여기 1st tx에 대해서만 isolation을 지켜주기 위한
        // 옵션은 SERIALIZABLE 이다.
        // REPEATABLE_READ -> SERIALIZABLE
        // SERIALIZEABLE은 내 tx가 실행되는 도중에 중간에 2nd tx가 실행되게 되었을 때,
        // 2nd tx가 내가 알고 있던 값(id 1)을 건드리지 않고 새로운 entity(id 2)를 추가하는 등의
        // 작업을 한다면, 그것은 신경쓰지 않고 자신의 로직을 진행한다.
        // 하지만 내가 findAll()과 같은 메서드를 실행하면, 2nd tx가 id 2를 추가하는 작업에 대해
        // 커밋을 하기 전까진 해당 findAll() 메서드에서 lock이 걸려서 wating하게 된다.
        // 즉, 2nd tx 가 id 2를 추가했다는 작업이 커밋되진 않았지만, 그런 tx가 있다는 것을
        // 인지하고 커밋될 때까지 waiting을 하게 된다. 그리고 2nd tx가 커밋되고,
        // 본인의 wating이 끝난 후에, 아직 tx가 끝나지 않은 상황에서 findAll()이 진행되면,
        // 2nd tx가 추가한 id 2 값이 보여진다. 그래서 tx 시작 시간에 따른 isolation이
        // 완전히 막아지진 않는다. 그리고 그 기반으로 전체 book table에 대한 update 작업도
        // 2nd tx의 id 2 값까지 포함되어 진행된다. 즉, 자신이 2nd tx보다 먼저 시작됐는데,
        // 중간에 2nd tx 로 인해 insert된 값을 읽을 수가 있게 된다.
        // 반면에 REPEATABLE_READ의 경우 중간에 2nd tx 로 인해 insert된 값을 읽을 수 없다.
        // 그래서 findAll()을 했을 때 id 2 값이 안 보이기 때문에, 이 상태에서 update():write를
        // 하게 되면, id 2에도 적용이 되어 개발자 입장에선 의도하지 않은 작업이 되게 된다.
        // REPEATABLE_READ 와 SERIALIZABLE 에 대해서 차이점을 정리하자면,
        // REPEATABLE_READ 의 내가 설정한 Transactional 메서드 안에서
        // 당연히 tx를 시작했을 때와 똑같은 값만 read가 되기 때문에 그 기반으로 전체 write를
        // 했는데 예기치 못한 phantom read로 인해 목표했던 로직이 어긋나게 되는 예기치 못한
        // 상황이 발생할 수도 있다. 근데 내가 아무리 tx를 온전히 잡는다 하더라도, write 작업에
        // 대한 것까지 내가 tx 시작한 시간을 기준으로 진행하는 것은 현실적으로 불가능하며, 이로 인해
        // 내가 tx를 잡았다고 해서 내가 write할 테이블들에 대해 tx를 시작한 시간부터 미리 lock을
        // 걸어서 다른 tx들의 접근을 막을 수는 없다. 왜냐하면, 애초에 tx를 시작할 때는
        // 내가 어떤 것에 write할지 모른다. 아무리 java 메서드 상에 작성해놨다 하더라도 실제론
        // runtime 으로 그 상황을 접해야만 알 수 있기 때문이다. SERIALIZABLE 도 마찬가지로
        // write 작업에 대해서 온전히 자신의 tx 시작 시간을 기준으로 처리해준다는 개념이 아니다.
        // write 작업은 runtime으로 그 상황을 실시간으로 접해야 write 한다는 것을 알 수 있다.
        // SERIALIZABLE 의 취지는 실시간으로 모든 tx들의 변경 값을 커밋된 기준으로
        // 반영해줄 수 있다 는 것이다.(물론 REPEATABLE_READ의 write 경우도 내 tx가 write할 때,
        // 다른 tx들의 commit이 완료될 때까지 lock이 걸려서 waiting하게 된다.)
        // REPEATABLE_READ의 경우 read 작업이 내 tx 시작 시간을
        // 기준으로 일관되게 같은 값을 읽기 때문에 그리고 그 기준으로 write를 하면
        // 그 기준으로 값이 변경될거라는 오해에 빠질 수 있다. 즉 개발자 입장에선 의도치 않은
        // 결과가 나올 수 있다는 것이다. SERIALIZABLE 은 실시간 커밋된 값을 lock을 통해서
        // 반영하기 때문에 REPEATABLE_READ 에서의 phantom_read 로 인한 의도치 않은
        // 결과를 개발자 입장에선 피할 수 있게 해준다.
        // SERIALIZABLE 의 경우 findAll() -> ... -> findAll() 사이에
        // 2nd tx로 인해 새로운 entity가 들어오게 되면 그 존재를 인지할 수 있기 때문에,
        // 그 최신 값을 기반으로 실시간으로 로직을 타게 된다.
        // 이런 것을 데이터 정합성이 100프로 맞다 라고 한다.
        // 다만 SERIALIZABLE 의 경우 2nd tx 같은 존재에 대해 lock을 통해 커밋을 기다려야
        // 하기 때문에 처리 속도가 느려지게 된다. READ_COMMITTED 의 경우도 내 tx가 시작된
        // 이후에 변경되고 commit된 값을 읽을 수 있지만, clear()를 하기 전까진 계속 자신의
        // 캐시에 있는 값을 일관되게 읽게 된다. 즉, clear()를 해줘야만 다른 tx에서 commit한
        // 값이 실시간으로 반영된다. 하지만, SERIALIZABLE 의 경우 자동으로 반영된다.
        // 따라서, READ_UNCOMMITTED는 정합성 때문에 거의 쓰지 않고,
        // SERIALIZABLE은 성능 때문에 쓰지 않는다.
        // 대부분 READ_COMMITTED 나 REPEATABLE_READ 의 격리 수준을 사용하게 된다.
        bookRepository.update();
        entityManager.clear();

        /* READ_UNCOMMITTED 테스트
        Book book = bookRepository.findById(id).get();
        book.setName("바뀔까?");
        bookRepository.save(book);
        */
    }

    @Transactional
    public List<Book> getAll() {

        List<Book> books = bookRepository.findAll();
        books.forEach(System.out::println);

        return books;
    }


}
