package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest     // 스프링 context를 로딩해서 테스트에 활용하겠다는 annotation
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void crud() { // create, read, update, delete

        userRepository.save(new User());

        System.out.println(">>> " + userRepository.findAll());
    }

}