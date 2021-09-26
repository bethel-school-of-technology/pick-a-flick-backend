package com.pickaflick.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickaflick.models.Tag;
import com.pickaflick.models.User;
import com.pickaflick.services.TagService;
import com.pickaflick.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/tags")
public class TagController {
	
	@Autowired
	private final TagService tagService;
	
	@Autowired
	private final UserService userService;
	
	public TagController(TagService tagService, UserService userService) {
		this.tagService = tagService;
		this.userService = userService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Tag>> getAllTags() {
		 List<Tag> tags = tagService.findAllTags();
		 return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Tag> getTagById(@PathVariable("id") Long id) {
		 Tag tag = tagService.findTagById(id);
		 return new ResponseEntity<>(tag, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Tag> addTag(@RequestBody Tag tag, Principal principal) {
		
		String username = principal.getName();
		User currentUser = userService.getUserByUsername(username);
		Long currentId = currentUser.getUserId();
		tag.setAuthorId(currentId);
		Tag newTag = tagService.addTag(tag);
		return new ResponseEntity<>(newTag, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Tag> updateTag(@PathVariable("id") Long id, @RequestBody Tag tag, Principal principal) { 
		// gets the name from the principal, which is the username
		String username = principal.getName();
		// gets the whole user profile from the username
		User currentUser = userService.getUserByUsername(username);
		// gets the userId from the user profile
		Long currentId = currentUser.getUserId();
		// sets the authorId to the userId
		tag.setAuthorId(currentId);
		// then saves the movie with updated info
		Tag updateTag = tagService.updateTag(tag);
		return new ResponseEntity<>(updateTag, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteTag(@PathVariable("id") Long id) {
		 tagService.deleteTag(id);
		 return new ResponseEntity<>(HttpStatus.OK);
	}
	
}