package com.sharebook.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user){
        // add a user
        User userOld = new User( "John", "Doe", "jodo@mail.de","1234");
        //TODO persist the user
        return new ResponseEntity(userOld, HttpStatus.CREATED);
    }

}
