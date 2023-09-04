package com.example.phone_Shop.Database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface User_Dao extends CrudRepository<User, Integer> {
    List<User> findAllByEmail(String email);
}
