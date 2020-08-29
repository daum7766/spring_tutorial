package com.example.demo.controller;

import java.util.Optional;
import java.util.regex.Pattern;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping(value = "/login")
    public Object Login(@RequestBody User loginUser) {
        Optional<User> user = userDao.findById(loginUser.getUid());
        if(!user.isPresent()) {
            return new ResponseEntity<>("id", HttpStatus.BAD_REQUEST);
        }
        if(passwordEncoder.matches(loginUser.getPassword(), user.get().getPassword())) {
            // 로그인 처리 jwt토큰 발급하기
            return new ResponseEntity<>("login", HttpStatus.OK);
        }
        return new ResponseEntity<>("password", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value="/signup")
    public Object Signup(@RequestBody User user) {
        // 비밀번호 패턴이 맞지않다면
        if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$", user.getPassword())){
            return new ResponseEntity<>("password", HttpStatus.BAD_REQUEST);
        }
        // 아이디가 중복된다면
        Optional<User> userIdChk = userDao.findById(user.getUid());
        if(userIdChk.isPresent()) {
            return new ResponseEntity<>("id", HttpStatus.BAD_REQUEST);
        }
        // 이메일이 중복된다면
        Optional<User> userEmailChk = userDao.findUserByEmail(user.getEmail());
        if(userEmailChk.isPresent()) {
            return new ResponseEntity<>("email", HttpStatus.BAD_REQUEST);
        }
        // 위의 조건을 모두 벗어났다면 비밀번호 인코딩해서 집어넣기
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userDao.save(user);
        // jwt 토큰 발급하기
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping(value="/logout")
    public Object Logout() {

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    

}