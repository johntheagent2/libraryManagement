package org.example.librarymanagement.controller.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${default-mapping}/test")
public class TestController {

    @GetMapping
    public String test(){
        return "Worked!";
    }
}
