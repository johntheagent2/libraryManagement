package org.example.librarymanagement.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${admin-mapping}/book")
@RestController
@AllArgsConstructor
public class ManageBookController {

    @GetMapping
    public void getSomething(){
//        TODO: Add get something here maybe book or something
    }
}
