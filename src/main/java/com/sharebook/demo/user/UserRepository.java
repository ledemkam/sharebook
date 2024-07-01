package com.sharebook.demo.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface UserRepository extends CrudRepository<UserSchema, Integer>{

   UserSchema findOneByEmail(String email);//findOneByEmail is a method that is used to find and return one user by email
}
