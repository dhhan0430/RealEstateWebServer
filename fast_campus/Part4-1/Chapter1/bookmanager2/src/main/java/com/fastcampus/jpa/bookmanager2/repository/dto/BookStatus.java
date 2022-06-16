package com.fastcampus.jpa.bookmanager2.repository.dto;

import lombok.Data;

@Data
public class BookStatus {

    private int code;
    private String description;

    public BookStatus(int code) {
        this.code = code;
        this.description = parseDescription(code);
    }

    public boolean isDisplayed() {
        return code == 200;
    }

    private String parseDescription(int code) {
        switch (code) {
            case 100:
                return "판매 종료";
            case 200:
                return "판매 중";
            case 300:
                return "판매 보류";
            default:
                return "미지원";
        }
    }
}
