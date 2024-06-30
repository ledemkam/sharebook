package com.sharebook.demo.book;

import com.sharebook.demo.user.UserSchema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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



}


