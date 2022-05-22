package com.example.response.controller;

import com.example.response.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// Spring에는 html 파일을 리턴하는 컨트롤러가 있다.
// HTML page를 리턴하는 서버

@Controller
public class PageController {

    // 이러한 컨트롤러에서는 return 자체가 String이면,
    // 자동으로 resources 디렉토리의 html 파일을 찾아간다.
    @RequestMapping("/main")
    public String main() {
        return "main.html";
    }

    // Controller에서 JSON 내려주는 방법 2가지
    // 1. ResponseEntity

    // 2. ResponseBody : Spring에서는 String으로 리턴하면 html 파일을 찾아서 리턴해주지만,
    // @ResponseBody를 붙여주면, 객체 자체를 리턴했을 때, 리소스를 찾지 않고
    // ResponseBody를 만들어서 response 해주겠다는 뜻.
    @ResponseBody
    @GetMapping("/user")
    public User user() {
        var user = new User(); // var : 타입 추론
        user.setName("steve");
        user.setAddress("fast campus");
        return user;
    }

}
