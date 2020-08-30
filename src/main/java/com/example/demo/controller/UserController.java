package com.example.demo.controller;

import java.util.Optional;
import java.util.regex.Pattern;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.User;
import com.example.demo.security.JwtResponse;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.security.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;
    
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
    
	@Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/login")
    public Object Login(@RequestBody User loginUser) throws Exception {
        authenticate(loginUser.getUid(), loginUser.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginUser.getUid());
                
        if(passwordEncoder.matches(loginUser.getPassword(), userDetails.getPassword())) {
            final String token = jwtTokenUtil.generateToken(userDetails);
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        }
        return new ResponseEntity<>("password", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value="/signup")
    public Object Signup(@RequestBody User user) {
        System.out.println(user);
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
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getUid());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @GetMapping(value="/logout")
    public Object Logout() {
        //TO DO
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}