package com.sharebook.demo.book;

import com.sharebook.demo.Borrows.BorrowRepository;
import com.sharebook.demo.user.UserRepository;
import com.sharebook.demo.user.UserSchema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Entity
public class BookSchema {

    @Id // this annotation is used to define the primary key
    @GeneratedValue (strategy = GenerationType.IDENTITY)// this annotation is used to define the strategy to generate the primary key
    private int id;

    @NotBlank() // this annotation is used to validate that the title is not empty(bzw title is required
    private String title;

    @ManyToOne // this annotation is used to define the relationship between the book and the category
    private Category category;

    @ManyToOne // this annotation is used to define the relationship between the book and the user
    private UserSchema user;

    @Transient // this annotation is used to ignore the field in the database
    private int categoryId;

    private BookStatus status;

    private Boolean deleted;



    public UserSchema getUserSchema() {
        return user;
    }

    public void setUserSchema(UserSchema user) {
        this.user = user;
    }



    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

   // this field is used to define if the book is deleted or not

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }


    @RestController
    public  class BookController {
        //we connect BookController to the database(Bookrepository) and get all the books

        @Autowired // this annotation is used to inject the BookRepository
        BookRepository bookRepository;

        @Autowired // this annotation is used to inject the userRepository
        UserRepository userRepository;

        @Autowired // this annotation is used to inject the categoryRepository
        CategoryRepository categoryRepository;

        @Autowired // this annotation is used to inject the BorrowRepository
        BorrowRepository borrowRepository;



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


        // this method is used to simulate the user is connected in to the application
        public  Integer getUserConnectedId(Principal principal) {
            if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
                throw  new RuntimeException("User not connected");
            }
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            UserSchema oneByEmail = userRepository.findOneByEmail(token.getName());
            return oneByEmail.getId();
        }
    }
}


