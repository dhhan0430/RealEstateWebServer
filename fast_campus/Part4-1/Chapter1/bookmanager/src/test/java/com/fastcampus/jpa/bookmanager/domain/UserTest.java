package com.fastcampus.jpa.bookmanager.domain;

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

        User user1 = new User(null, "steve", "steve@fastcampus",
                                LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User("steve", "steve@fastcampus");

        System.out.println(">>> " + user.toString());
    }
}