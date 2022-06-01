package com.example.restaurantnaverapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/main")
    public ModelAndView main() {
        // "main" : templates 디렉토리 하위 경로
        // localhost:8080/page/main 에 접근하면 templates 디렉토리의 main.html 을 리턴해준다.
        return new ModelAndView("main");
    }
}
