package com.sharebook.demo.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<UserSchema, Integer>{

    List<UserSchema> findByEmail(String email);
}
