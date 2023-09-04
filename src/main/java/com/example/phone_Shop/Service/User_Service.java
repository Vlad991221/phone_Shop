package com.example.phone_Shop.Service;

import com.example.phone_Shop.Database.User;
import com.example.phone_Shop.Database.User_Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class User_Service {
    @Autowired
    User_Dao userDao;

    public void registerUser(String email, String password1, String password2) throws User_Exception {
        if (!password1.equals(password2)) {
            throw new User_Exception("Passwords are not identical!");
        }

        User user = new User();
        user.setPassword(password1);
        user.setEmail(email);
        userDao.save(user);
    }

    public List<User> loginUser(String email, String password) throws User_Exception {
        List<User> userList = userDao.findAllByEmail(email);

        if (userList.isEmpty()) {
            throw new User_Exception("Incorrect User/Password");
        }

        User user = userList.get(0);

        if (!user.getPassword().equals(password)) {
            throw new User_Exception("Incorrect User/Password");
        }

        return userList;
    }
}
