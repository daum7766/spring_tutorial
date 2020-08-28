package com.example.demo.controller;

import java.util.Optional;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class UserController {
    @Autowired
    UserDAO userDao;

    @PostMapping(value="/signup")
    public Object Signup(@RequestBody User user) {
        Optional<User> userIdChk = userDao.findById(user.getUid());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println(user);
        System.out.println(user.getUid());
        if(userIdChk.isPresent()) {
            System.out.println("이미있는아이디");
        }
        else {
            System.out.println("없는아이디아이디");
        }
        return "true";
    }
    

}