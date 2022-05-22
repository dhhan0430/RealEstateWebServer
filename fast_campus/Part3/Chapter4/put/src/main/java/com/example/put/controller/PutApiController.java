package com.example.put.controller;

import com.example.put.PostRequestDto.PutRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PutApiController {

    @PutMapping("/put/{userId}")
    public PutRequestDto put(@RequestBody PutRequestDto requestDto,
                             @PathVariable(name = "userId") Long id) {

        System.out.println(id);

        // Spring에서 알아서 json 형태로 return 해줌.
        return requestDto;
    }
}
