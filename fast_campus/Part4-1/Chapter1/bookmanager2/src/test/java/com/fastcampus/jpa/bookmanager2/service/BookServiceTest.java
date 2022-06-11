package com.fastcampus.jpa.bookmanager2.service;

import com.fastcampus.jpa.bookmanager2.domain.Book;
import com.fastcampus.jpa.bookmanager2.repository.AuthorRepository;
import com.fastcampus.jpa.bookmanager2.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void transactionTest() {

        try {
            bookService.putBookAndAuthor();
            // 이런식으로 @Transactional이 적용된 메서드를 한 번 wrapping 해서
            // 호출하면 RuntimeException을 발생시켜도 롤백되지 않는다.
            // 이것은 스프링 컨테이너와 관련이 있는데, 스프링 컨테이너는 Bean 으로 진입할 때
            // 걸려있는 annotation에 대해 처리하도록 되어 있다. 이미 put() 메서드에
            // 진입하는 순간, Bean 내부로 들어온 것이고, 이렇게 진입한 이후에,
            // Bean 내부에 있는 다른 메서드를 호출하게 되면, 그 메서드에 있는 @Transactional 은
            // 무시가 된다. 해당 annotation의 효과가 전혀 없다.
            // 왜냐하면 Bean 내부에서 호출이 되는 시점에 aop 에서 annotation을 읽어서
            // 처리하기 때문이다.
            // bookService.put();
        } catch (RuntimeException e) {
            System.out.println(">>> " + e.getMessage());
        }

        System.out.println("book: " + bookRepository.findAll());
        System.out.println("author: " + authorRepository.findAll());
    }

    // propagation test
    @Test
    void transactionTest2() {

        try {
            bookService.putBookAndAuthor2();
        } catch (RuntimeException e) {
            System.out.println(">>> " + e.getMessage());
        }

        System.out.println("book: " + bookRepository.findAll());
        System.out.println("author: " + authorRepository.findAll());
    }

    @Test
    void isolationTest() {
        Book book = new Book();
        book.setName("JPA 강의");
        bookRepository.save(book);

        bookService.get(1L);

        System.out.println(">>> " + bookRepository.findAll());

    }
}