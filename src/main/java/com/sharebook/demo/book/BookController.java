package com.sharebook.demo.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class BookController {

    @GetMapping("/books")
    public ResponseEntity listBooks() {
        // list all books
        Book book = new Book();
        book.setTitle("My books");
        book.setCategory(new Category("BD"));

        return new ResponseEntity(Arrays.asList(book), HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity addBook(@RequestBody Book book){
        // add a book
        //TODO persist the book
        return new ResponseEntity(book,HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId){
        // delete a book
        //TODO delete seit bookId
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId,@RequestBody Book book){
        // update a book
        //TODO update book mit bookId
        return new ResponseEntity(book,HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity listCategories(){
        Category category = new Category("Historisch");
        Category category2 = new Category("polizei");

        return new ResponseEntity(Arrays.asList(category,category2),HttpStatus.OK);
    }

}
