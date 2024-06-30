package com.sharebook.demo.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/users")
    public ResponseEntity addUser(@Valid @RequestBody UserSchema userSchema) {

        // exist user?
        List<UserSchema> userSchemas = userRepository.findByEmail(userSchema.getEmail());

        if (!userSchemas.isEmpty()) {
            return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
        }

        userRepository.save(userSchema);

        return new ResponseEntity(userSchema, HttpStatus.CREATED);

    }


}
