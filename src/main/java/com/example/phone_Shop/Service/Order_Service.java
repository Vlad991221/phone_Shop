package com.example.phone_Shop.Service;

import com.example.phone_Shop.Database.Order;
import com.example.phone_Shop.Database.Order_Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Order_Service {
    @Autowired
    Order_Dao orderDao;

    public List<Order> getOrdersByUserId(int id) {
        return orderDao.findAllByUserId(id);
    }
}
