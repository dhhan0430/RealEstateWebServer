package com.fastcampus.jpa.bookmanager2.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookNameAndCategory {

    private String name;
    private String Category;

}

/* 인터페이스로도 jpql을 통해 가져올 수 있다.
public interface BookNameAndCategory {

    String getName();
    String getCategory();

}
*/