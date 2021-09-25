package com.pickaflick.services;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.User;
import com.pickaflick.repos.IUserRepo;

@Service
public class UserService implements UserDetailsService{
	
	private final IUserRepo userRepo;
	
	@Autowired
	public UserService(IUserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities());
	}

	public UserDetails Save(User newUser) {
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		User savedUser = userRepo.save(newUser);
		return new org.springframework.security.core.userdetails.User(savedUser.getUsername(), savedUser.getPassword(), getAuthorities());
	}
	private List<SimpleGrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authList = new ArrayList<>();
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authList;
	}
	//added code below.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}
	
	public User findUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User by id " + id + " was not found."));
	}

	// Comments from Jared's original code:

	// SetUserId needs to be Long for it to be a many to many. I have used setFirstName, 
	// but this may need to be changed.
	// Above comments were for previous code that is commented out below under addUser() - not sure if we need to do this or not.
	public User addUser(User user) {
		// Don't think we need this, but didn't want to delete just in case...
		// user.setFirstName(UUID.randomUUID().toString());
		return userRepo.save(user);
	}
	
	public User updateUser(User user) {
		return userRepo.save(user);
	}
	
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

}