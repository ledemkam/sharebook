package com.sharebook.demo.user;

import com.sharebook.demo.jwt.JwtController;
import com.sharebook.demo.jwt.JwtFilter;
import com.sharebook.demo.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.sharebook.demo.jwt.JwtFilter.AUTHORIZATION_HEADER;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtController jwtController;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/users")
    public ResponseEntity addUser(@Valid @RequestBody UserSchema userSchema) {

        // exist user?
        UserSchema existingUser = userRepository.findOneByEmail(userSchema.getEmail());

        if (existingUser!= null) {
            return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
        }

        UserSchema user = saveUser(userSchema);
        Authentication authentication = jwtController.logUser(userSchema.getEmail(), userSchema.getPassword());
        String jwt = jwtUtils.generateToken(authentication); // generate token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity(user, httpHeaders, HttpStatus.OK);
    }

     // this method is used if the user is connected(or online)
     @GetMapping("/isConnected")
     public ResponseEntity getUSerConnected() {
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (principal instanceof UserDetails) {
             return new ResponseEntity(((UserDetails) principal).getUsername(), HttpStatus.OK);
         }
         return new ResponseEntity("User is not connected", HttpStatus.FORBIDDEN);
     }



    private UserSchema saveUser(UserSchema userSchema) {
        UserSchema user = new UserSchema();
        user.setEmail(userSchema.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userSchema.getPassword()));
        user.setLastName(StringUtils.capitalize(userSchema.getLastName()));
        user.setFirstName(StringUtils.capitalize(userSchema.getFirstName()));
        userRepository.save(user);
        return user;
    }

}
