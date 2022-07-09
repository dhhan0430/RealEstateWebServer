package com.fastcampus.jpa.bookmanager2.repository;

import com.fastcampus.jpa.bookmanager2.domain.cascadepractice.Child;
import com.fastcampus.jpa.bookmanager2.domain.cascadepractice.Parent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParentRepositoryTest {

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ChildRepository childRepository;

    @Test
    public void cascadeTest() {

        Parent parent = new Parent();
        parent.setName("parent");
        //parentRepository.save(parent);

        Child child1 = new Child();
        child1.setName("child1");
        Child child2 = new Child();
        child2.setName("child2");

        parent.getChildList().add(child1);
        parent.getChildList().add(child2);

        parentRepository.save(parent);

        System.out.println("/----------------------------------");
        System.out.println("parent: " + parentRepository.findAll());
        System.out.println("child: " + childRepository.findAll());
        System.out.println("----------------------------------/");

        //parentRepository.delete(parent);
        parent.getChildList().remove(0);
        parentRepository.save(parent);
//        parentRepository.delete(parent);

        System.out.println("/----------------------------------");
        System.out.println("parent: " + parentRepository.findAll());
        System.out.println("child: " + childRepository.findAll());
        System.out.println("----------------------------------/");

        parentRepository.deleteById(1L);

        System.out.println("/----------------------------------");
        System.out.println("parent: " + parentRepository.findAll());
        System.out.println("child: " + childRepository.findAll());
        System.out.println("----------------------------------/");


        // practice
        var p1 = new Parent();
        p1.setName("p1");
        parentRepository.save(p1);
        var p2 = new Parent();
        p2.setName("p1");
        parentRepository.save(p2);
        System.out.println("practice: " + parentRepository.findAll());
        System.out.println("practice1: " + parentRepository.findByName("p2"));

    }
}