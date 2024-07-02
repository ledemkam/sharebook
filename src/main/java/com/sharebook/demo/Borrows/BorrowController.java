package com.sharebook.demo.Borrows;

import com.sharebook.demo.book.BookSchema;
import com.sharebook.demo.book.BookController;
import com.sharebook.demo.book.BookRepository;
import com.sharebook.demo.book.BookStatus;
import com.sharebook.demo.user.UserSchema;
import com.sharebook.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class BorrowController {

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookController bookController;




    @GetMapping("/borrows")
    public ResponseEntity myBorrowsList(Principal principal){
        // list all borrows
        List<BorrowSchema> borrowSchemas = borrowRepository.findByBorrowerId(bookController.getUserConnectedId(principal));
        return new ResponseEntity(borrowSchemas, HttpStatus.OK);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId")String bookId, Principal principal){
        //add a borrow
        Integer userConnectedId = bookController.getUserConnectedId(principal);//user musst connected
        Optional<UserSchema> borrower = userRepository.findById(userConnectedId);//get the user connected
        Optional<BookSchema> book = bookRepository.findById(Integer.valueOf(bookId));

        if(borrower.isPresent() && book.isPresent() && book.get().getStatus().equals(BookStatus.FREE)){
            BorrowSchema borrowSchema = new BorrowSchema();//create a new borrow
            BookSchema bookSchemaEntity = book.get();
            borrowSchema.setBookSchema(bookSchemaEntity);//set the book
            borrowSchema.setBorrower(borrower.get());//set the borrower
            borrowSchema.setLender(book.get().getUserSchema());//set the lender
            borrowSchema.setAskDate(LocalDate.now());//set the ask date
            borrowRepository.save(borrowSchema);//save the borrow in the database

            //change the status of the book(mettre a jour le status du livre)
            bookSchemaEntity.setStatus(BookStatus.BORROWED);
            bookRepository.save(bookSchemaEntity);
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return  new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity deleteBorrowId(@PathVariable("borrowId") String borrowId){
        //delete a borrow
        Optional<BorrowSchema> borrower = borrowRepository.findById(Integer.valueOf(borrowId));
        if(borrower.isEmpty()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        BorrowSchema borrowSchemaEntity = borrower.get();//get the borrow
        borrowSchemaEntity.setCloseDate((LocalDate.now()));//set the close date
        borrowRepository.save(borrowSchemaEntity);//save the borrow in the database


        //change the status of the book(mettre a jour le status du livre)
        BookSchema bookSchema = borrowSchemaEntity.getBookSchema();
        bookSchema.setStatus(BookStatus.FREE);
        bookRepository.save(bookSchema);

        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }
}