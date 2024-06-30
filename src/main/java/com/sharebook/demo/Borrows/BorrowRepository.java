package com.sharebook.demo.Borrows;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BorrowRepository extends CrudRepository<BorrowSchema, Integer>{

    List<BorrowSchema> findByBorrowerId(Integer borrowId);
    List<BorrowSchema> findByBookId(Integer bookId);

}
