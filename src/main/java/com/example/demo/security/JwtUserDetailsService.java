package com.example.demo.security;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserDAO userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userDao.findById(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.get().getUid(), user.get().getPassword(),
				new ArrayList<>());
    }
}