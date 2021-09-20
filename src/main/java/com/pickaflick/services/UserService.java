package com.pickaflick.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.User;
import com.pickaflick.repos.IUserRepo;

@Service
public class UserService {
	
	private final IUserRepo userRepo;
	
	@Autowired
	public UserService(IUserRepo userRepo) {
		this.userRepo = userRepo;
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