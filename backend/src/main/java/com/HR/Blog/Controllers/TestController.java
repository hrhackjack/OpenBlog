package com.HR.Blog.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("http://localhost:3000")
@RequestMapping("/api")
public class TestController {
    @GetMapping("/hrx")
    public ResponseEntity<String> getPostsByCategory() {
        String message = "Hello HR, This is a Secure EndPoint!";
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
}

