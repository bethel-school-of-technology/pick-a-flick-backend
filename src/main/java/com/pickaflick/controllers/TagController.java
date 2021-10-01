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

import com.pickaflick.exceptions.UnauthorizedException;
import com.pickaflick.models.Movie;
import com.pickaflick.models.Tag;
import com.pickaflick.models.User;
import com.pickaflick.services.TagService;
import com.pickaflick.services.UserService;

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

//	@GetMapping("/all")
//	public ResponseEntity<List<Tag>> getAllTags() {
//		 List<Tag> tags = tagService.findAllTags();
//		 return new ResponseEntity<>(tags, HttpStatus.OK);
//	}

	// Gets all the tags - checks that userId matches authorId first
	@GetMapping("/all")
	public ResponseEntity<List<Tag>> getAllTags(Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		Long authorId = currentUserId;
		List<Tag> tags = tagService.findTagsByAuthorId(authorId);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

//	@GetMapping("/find/{id}")
//	public ResponseEntity<Tag> getTagById(@PathVariable("id") Long id) {
//		 Tag tag = tagService.findTagById(id);
//		 return new ResponseEntity<>(tag, HttpStatus.OK);
//	}

	// Gets tag by id - checks that userId matches authorId first
	@GetMapping("/find/{id}")
	public ResponseEntity<Tag> getTagById(@PathVariable("id") Long id, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);

		Tag tag = tagService.findTagById(id);

		if (tag.getAuthorId() == currentUserId) {
			return new ResponseEntity<>(tag, HttpStatus.OK);
		} 
		else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}

	// Adds new tag - gets userId and assigns it to authorId
	@PostMapping("/add")
	public ResponseEntity<Tag> addTag(@RequestBody Tag tag, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		tag.setAuthorId(currentUserId);
		Tag newTag = tagService.addTag(tag);
		return new ResponseEntity<>(newTag, HttpStatus.CREATED);
	}

//	@PutMapping("/update/{id}")
//	public ResponseEntity<Tag> updateTag(@PathVariable("id") Long id, @RequestBody Tag tag, Principal principal) { 
//		Long currentUserId = userService.getUserIdFromPrincipal(principal);	
//		// sets the authorId to the userId
//		tag.setAuthorId(currentUserId);
//		// then saves the tag with updated info
//		Tag updateTag = tagService.updateTag(tag);
//		return new ResponseEntity<>(updateTag, HttpStatus.OK);
//	}

	// Updates tag - checks that userId matches authorId first
	@PutMapping("/update/{id}")
	public ResponseEntity<Tag> updateTag(@PathVariable("id") Long id, @RequestBody Tag tag, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// gets the tag by the id passed in
		Tag tagById = tagService.findTagById(id);
		// gets the authorId of that tag
		Long tagAuthorId = tagById.getAuthorId();

		if (currentUserId == tagAuthorId) {
			// saves the tag with updated info
			Tag updateTag = tagService.updateTag(tag);
			return new ResponseEntity<>(updateTag, HttpStatus.OK);
		} else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}

//	@DeleteMapping("/delete/{id}")
//	public ResponseEntity<?> deleteTag(@PathVariable("id") Long id) {
//		 tagService.deleteTag(id);
//		 return new ResponseEntity<>(HttpStatus.OK);
//	}

	// Deletes tag - checks that userId matches authorId first
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteTag(@PathVariable("id") Long id, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// gets the tag by the id passed in
		Tag tagById = tagService.findTagById(id);
		// gets the authorId of that tag
		Long tagAuthorId = tagById.getAuthorId();
		
		if(currentUserId == tagAuthorId) {
		 tagService.deleteTag(id);
		 return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}
}