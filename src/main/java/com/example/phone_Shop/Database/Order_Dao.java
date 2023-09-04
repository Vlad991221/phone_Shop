package com.example.phone_Shop.Database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Order_Dao extends CrudRepository<Order, Integer> {
    List<Order> findAllByUserId(int id);
}
