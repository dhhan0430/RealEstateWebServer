package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.*;
import com.fastcampus.jpa.bookmanager2.repository.dto.BookStatus;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void bookTest() {
        Book book = new Book();
        book.setName("Jpa 초격차 패키지");
        book.setAuthorId(1L);
        //book.setPublisherId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());
    }

    @Test
    @Transactional
    void bookRelationTest() {

        givenBookAndReview();

        User user = userRepository.findByEmail("martin@fastcampus.com");

        System.out.println("Review: " + user.getReviews());
        System.out.println("Book: " + user.getReviews().get(0).getBook());
        System.out.println("Publisher: "
                + user.getReviews().get(0).getBook().getPublisher());
    }

    //@Transactional
    @Test
    void bookCascadeTest() {
        Book book = new Book();
        book.setName("JPA 초격차 패키지");
//        bookRepository.save(book);

        Publisher publisher = new Publisher();
        publisher.setName("패스트 캠퍼스");

        book.setPublisher(publisher);
        book.setStatus(new BookStatus(200));
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setName("JPA 초격차 패키지2");
        book2.setStatus(new BookStatus(200));
        var savedPublisher = publisherRepository.findById(1L).get();
        System.out.println("savedPublisher: " + savedPublisher);
        book2.setPublisher(savedPublisher);
        bookRepository.save(book2);

//        publisher.getBooks().add(book);
//        publisherRepository.save(publisher);

        System.out.println("------------------------------------------");
        System.out.println("publisher0: " + bookRepository.findAll().get(0).getPublisher());
        System.out.println("------------------------------------------");

        // publisher.getBooks().add(book);
        // 아래가 더 가독성 있는 코드
        // 영속성 전이를 위해 지움
//        publisher.addBook(book);
//        publisherRepository.save(publisher);

        System.out.println("books: " + bookRepository.findAll());
        System.out.println("publishers1: " + publisherRepository.findAll());

        Book book1 = bookRepository.findById(1L).get();
        book1.getPublisher().setName("슬로우 캠퍼스");

        bookRepository.save(book1);

        System.out.println("publishers2: " + publisherRepository.findAll());


//        Book book2 = bookRepository.findById(1L).get();
//        bookRepository.delete(book2);

        Book book3 = bookRepository.findById(1L).get();
//        book3.setPublisher(null);
//        bookRepository.save(book3);
        bookRepository.delete(book3);

//        Publisher publisher1 = publisherRepository.findById(1L).get();
//        System.out.println("publisher1 book list1: " + publisher1.getBooks());
//        publisher1.getBooks().remove(0);
//        System.out.println("publisher1 book list2: " + publisher1.getBooks());

        System.out.println("books: " + bookRepository.findAll());
        System.out.println("publisher3: " + publisherRepository.findAll());
        System.out.println("book3-publisher: " +
                bookRepository.findById(1L).get().getPublisher());
    }

    @Test
    void bookRemoveCascadeTest() {
        System.out.println("book0: " + bookRepository.findAll());

        bookRepository.deleteById(1L);

        System.out.println("book: " + bookRepository.findAll());
        System.out.println("publisher: " + publisherRepository.findAll());

        bookRepository.findAll().forEach(
                book -> System.out.println(book.getPublisher())
        );
    }

    @Test
    void softDelete() {
        bookRepository.findAll().forEach(System.out::println);
        System.out.println(bookRepository.findById(3L));

//        bookRepository.findByCategoryIsNull().forEach(System.out::println);
//        bookRepository.findAllByDeletedFalse().forEach(System.out::println);
//        bookRepository.findByCategoryIsNullAndDeletedFalse().forEach(System.out::println);
    }

    @Test
    void queryTest() {
        bookRepository.findAll().forEach(System.out::println);
        System.out.println("findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual: "
        + bookRepository.findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(
                "JPA 초격차 패키지",
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().minusDays(1L)
        ));

        System.out.println("findByNameRecently: "
        + bookRepository.findByNameRecently(
                "JPA 초격차 패키지",
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().minusDays(1L)
        ));

        System.out.println(bookRepository.findBookNameAndCategory());

        bookRepository.findBookNameAndCategory().forEach(
                //tuple -> System.out.println(tuple.get(0) + ":" + tuple.get(1))
                b -> System.out.println(b.getName() + ":" + b.getCategory())
        );

        bookRepository.findBookNameAndCategory(
                PageRequest.of(1, 1)).forEach(
                        bookNameAndCategory -> System.out.println(
                                bookNameAndCategory.getName() + ":"
                                + bookNameAndCategory.getCategory()
                        )
        );

        bookRepository.findBookNameAndCategory(
                PageRequest.of(0, 1)).forEach(
                bookNameAndCategory -> System.out.println(
                        bookNameAndCategory.getName() + ":"
                                + bookNameAndCategory.getCategory()
                )
        );
    }

    @Test
    void nativeQueryTest() {
    //    bookRepository.findAll().forEach(System.out::println);
    //    bookRepository.findAllCustom().forEach(System.out::println);

        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            book.setCategory("IT전문서");
        }

        // update 진행할 때 entity 하나하나씩 update하게 되며 이는 성능 저하를 유발.
        bookRepository.saveAll(books);

        System.out.println(bookRepository.findAll());

        System.out.println("affected rows: " + bookRepository.updateCategories());
        bookRepository.findAllCustom().forEach(System.out::println);

        System.out.println(bookRepository.showTables());

    }

    @Test
    void converterTest() {
        bookRepository.findAll().forEach(System.out::println);

        Book book = new Book();
        book.setName("또다른 IT 전문 서적");
        //book.setStatus(new BookStatus(200));

        bookRepository.save(book);

        System.out.println(bookRepository.findRawRecord().values());
    }

    private void givenBookAndReview() {
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("패스트캠퍼스");

        return publisherRepository.save(publisher);
    }

    private User givenUser() {
        return userRepository.findByEmail("martin@fastcampus.com");
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("JPA 초격차 패키지");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("내 인생을 바꾼 책");
        review.setContent("너무너무 재미있고 즐거운 책이었어요.");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }


}