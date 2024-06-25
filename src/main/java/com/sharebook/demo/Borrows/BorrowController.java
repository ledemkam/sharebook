package com.sharebook.demo.Borrows;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class BorrowController {

    @GetMapping("/borrows")
    public ResponseEntity getMyBorrows(){
        // list all borrows
        //TODO
        Borrow borrow = new Borrow();
        borrow.setAskDate(LocalDate.now());
        return new ResponseEntity(borrow, HttpStatus.OK);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId")String bookId){
        //add a borrow
        return  new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity deleteBorrowId(@PathVariable("borrowId") String borrowId){
        return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
