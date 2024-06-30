package com.sharebook.demo.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository// this annotation is used to define the class as a repository and
// it is used to access the database
public interface   BookRepository extends CrudRepository<BookSchema, Integer>{
    // this interface is used to define the repository of the book
    // this interface extends the CrudRepository interface
    // the first parameter is the Table that we want to create the repository for it
    // the second parameter is the type of the primary key

    // this method is used to get the list of the books that have the status and the user id
    List<BookSchema> findByStatusAndUserIdNotAndDeletedFalse(BookStatus status, Integer userId);

    // this method is used to get the list of the books that have the user
    List<BookSchema> findByUserIdAndDeletedFalse(Integer userId);

}
