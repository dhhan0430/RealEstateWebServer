package com.example.put.controller;

import com.example.put.PostRequestDto.PostRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PutApiController {

    @PutMapping("/put/{userId}")
    public PostRequestDto put(@RequestBody PostRequestDto requestDto,
                              @PathVariable(name = "userId") Long id) {

        System.out.println(id);

        // Spring에서 알아서 json 형태로 return 해줌.
        return requestDto;
    }
}
