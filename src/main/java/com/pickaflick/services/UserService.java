package com.pickaflick.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pickaflick.exceptions.AlreadyExistsException;
import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.User;
import com.pickaflick.repos.IUserRepo;

@Service
public class UserService implements UserDetailsService {

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
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthorities());
	}

	public User findUserByUsername(String username) {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new NotFoundException("Username " + username + " was not found.");
		}
		else {
			return user;
		}
	}

	public Long getUserIdFromPrincipal(Principal principal) {
		// gets the name from the principal, which is the username
		String username = principal.getName();
		// gets the whole user profile from the username
		User currentUser = this.findUserByUsername(username);
		// gets the userId from the user profile
		Long currentId = currentUser.getUserId();
		return currentId;
	}

	public UserDetails addUser(User newUser) {
		// gets proposed new username
		String newUsername = newUser.getUsername();
		// tries to find that new username in the DB to see if it already exists
		User existingUser = userRepo.findByUsername(newUsername);
		// if nothing found, then okay to create new user with that username:
		if (existingUser == null) {
			newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
			User savedUser = userRepo.save(newUser);
			return new org.springframework.security.core.userdetails.User(savedUser.getUsername(),
					savedUser.getPassword(), getAuthorities());
			// if username was found, return message that username already exists
		} else {
			throw new AlreadyExistsException("Username " + newUsername + " already exists - please choose a different username.");
		}
	}

	private List<SimpleGrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authList = new ArrayList<>();
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authList;
	}

	public List<User> findAllUsers() {
		return userRepo.findAll();
	}

	public User findUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User by id " + id + " was not found."));
	}

	public User updateUser(User user) {
		return userRepo.save(user);
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}
}
