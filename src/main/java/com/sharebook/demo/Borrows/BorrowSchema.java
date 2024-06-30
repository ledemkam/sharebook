package com.sharebook.demo.Borrows;


import com.sharebook.demo.book.BookSchema;
import com.sharebook.demo.user.UserSchema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class BorrowSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private UserSchema borrower;

    @ManyToOne
    private UserSchema lender;

    @ManyToOne
    private BookSchema book;

    private LocalDate askDate;

    private LocalDate closeDate;

    public BookSchema getBookSchema() {
        return book;
    }

    public void setBookSchema(BookSchema book) {
        this.book = book;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAskDate() {
        return askDate;
    }

    public void setAskDate(LocalDate askDate) {
        this.askDate = askDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }




    public UserSchema getBorrower() {
        return borrower;
    }

    public void setBorrower(UserSchema borrower) {
        this.borrower = borrower;
    }

    public UserSchema getLender() {
        return lender;
    }

    public void setLender(UserSchema lender) {
        this.lender = lender;
    }


}
