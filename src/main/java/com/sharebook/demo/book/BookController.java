package com.sharebook.demo.book;

import com.sharebook.demo.Borrows.BorrowSchema;
import com.sharebook.demo.Borrows.BorrowRepository;
import com.sharebook.demo.user.UserSchema;
import com.sharebook.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class BookController {
    //we connect BookController to the database(Bookrepository) and get all the books

    @Autowired // this annotation is used to inject the BookRepository
    private BookRepository bookRepository;

    @Autowired // this annotation is used to inject the userRepository
    private UserRepository userRepository;

    @Autowired // this annotation is used to inject the categoryRepository
    private CategoryRepository categoryRepository;

    @Autowired // this annotation is used to inject the BorrowRepository
    private BorrowRepository borrowRepository;

    @GetMapping("/books")
    public ResponseEntity listBooks(@RequestParam(required = false) BookStatus status, Principal principal) {
        Integer userConnectedId =  this.getUserConnectedId(principal);//  the user connected is the user who is connected to the application
        List<BookSchema> books;

        if(status != null && status == BookStatus.FREE) {
            //free books
            books = bookRepository.findByStatusAndUserIdNotAndDeletedFalse(status, userConnectedId);
        }else {
            //My Books
            books = bookRepository.findByUserIdAndDeletedFalse(userConnectedId);
        }
        return new ResponseEntity(books, HttpStatus.OK);
    }

    public  Integer getUserConnectedId(Principal principal){
        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            throw new RuntimeException(("User not found"));
        }
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        UserSchema oneByEmail = userRepository.findOneByEmail(token.getName());

        return oneByEmail.getId();
    }// this method is used to simulate the user is connected in to the application


    @PostMapping("/books")
    public ResponseEntity addBook(@RequestBody @Valid BookSchema book, Principal principal){
        // add a book
        Integer userConnectedId =  this.getUserConnectedId(principal);// the user connected is the user who is connected to the application
        Optional<UserSchema> userToAdd = userRepository.findById(userConnectedId);
        Optional<Category> categoryToAdd = categoryRepository.findById(book.getCategoryId());
        if(categoryToAdd.isPresent()){
            book.setCategory(categoryToAdd.get());
        }else {
            return new ResponseEntity("Category not found,you must it provide",HttpStatus.BAD_REQUEST);
        }
        if(userToAdd.isPresent()){
            book.setUserSchema(userToAdd.get());
        } else {
            return new ResponseEntity("User not found,you must it provide",HttpStatus.BAD_REQUEST);
        }
        book.setDeleted(false);// this field is used to define if the book by default is deleted or not
        book.setStatus(BookStatus.FREE);// this field is used to define by default the status of the book
        bookRepository.save(book);// this method is used to save the book in the database
        return new ResponseEntity(book,HttpStatus.CREATED);
    }


    @DeleteMapping("/books/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId){
        // delete a book
        Optional<BookSchema> bookTodelete = bookRepository.findById(Integer.valueOf(bookId));
        if(!bookTodelete.isPresent()){
            return new ResponseEntity("Book not found",HttpStatus.BAD_REQUEST);
        }
        BookSchema updatedBook = bookTodelete.get();// this method is used to get the book from the database
        List<BorrowSchema> borrows = borrowRepository.findByBookId(updatedBook.getId());// this method is used to get the list of the borrows that have the book id
        for (BorrowSchema borrowSchema : borrows) {
            if(borrowSchema.getCloseDate() == null){
                UserSchema borrower = borrowSchema.getBorrower();
                return new ResponseEntity(borrower,HttpStatus.CONFLICT);
            }
        }
        updatedBook.setDeleted(true);// this field is used to define if the book is deleted or not
        bookRepository.save(updatedBook);// this method is used to save the book in the database
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId,@RequestBody @Valid BookSchema book) {
        // update a book
        Optional<BookSchema> bookToUpdate = bookRepository.findById(Integer.valueOf(bookId));
        if (!bookToUpdate.isPresent()) {
            return new ResponseEntity("Book not existing", HttpStatus.BAD_REQUEST);
        }
        BookSchema bookSchemaToSave = bookToUpdate.get();// this method is used to get the book from the database
        Optional<Category> newCategory = categoryRepository.findById(book.getCategoryId());
        bookSchemaToSave.setCategory(newCategory.get());
        bookSchemaToSave.setTitle(book.getTitle());
        bookRepository.save(bookSchemaToSave);// this method is used to save the book in the database
        return new ResponseEntity(bookSchemaToSave, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity listCategories() {
        //list all  categories
        // this method is used to get all the categories
        return new ResponseEntity(categoryRepository.findAll(), HttpStatus.OK);
    }

    // this method is used to load ein book
    @GetMapping("/books/{bookId}")//bookId is the id of the book,corresponding to the book that we want to load
    public ResponseEntity loadingBook(@PathVariable("bookId") String bookId) {
        Optional<BookSchema> book = bookRepository.findById(Integer.valueOf(bookId));
        if(!book.isPresent()){
            return new ResponseEntity("Book not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(book.get(),HttpStatus.OK);


    }
}