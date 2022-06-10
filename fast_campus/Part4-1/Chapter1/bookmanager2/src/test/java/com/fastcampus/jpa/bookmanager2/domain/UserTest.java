package com.fastcampus.jpa.bookmanager2.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void test() {
        User user = new User();
        user.setEmail("steve@fastcampus.com");
        user.setName("steve");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        /*
        User user1 = new User(null, "steve", "steve@fastcampus.com",
                                LocalDateTime.now(), LocalDateTime.now());
        */
        User user2 = new User("steve", "steve@fastcampus.com");

        System.out.println(">>> " + user.toString());
    }
}