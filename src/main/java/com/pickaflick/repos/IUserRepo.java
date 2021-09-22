package com.pickaflick.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickaflick.models.User;

public interface IUserRepo extends JpaRepository<User, Long>{

	User findByUsername(String username);

}


