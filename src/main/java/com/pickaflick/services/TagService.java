package com.pickaflick.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.Tag;
import com.pickaflick.repos.ITagRepo;

@Service
public class TagService {
	
	private final ITagRepo tagRepo;
	
	@Autowired
	public TagService(ITagRepo tagRepo) {
		this.tagRepo = tagRepo;
	}
	
	public List<Tag> findAllTags() {
		return tagRepo.findAll();
	}
	
	public Tag findTagById(Long id) {
		return tagRepo.findById(id).orElseThrow(() -> new NotFoundException("Tag by id " + id + " was not found."));
	}
	
	// Comments from Jared's original code:
	
	// SetTagId needs to be Long for it to be a many to many. I have used setTagName, 
	// but this may need to be changed.
	// Above comments were for previous code that is commented out below under addMovies() - not sure if we need to do this or not.
	public Tag addTag(Tag tag) {
		// Don't think we need this, but didn't want to delete just in case...
		// tags.setTagName(UUID.randomUUID().toString());
		return tagRepo.save(tag);
	}
	
	public Tag updateTag(Tag tag) {
		return tagRepo.save(tag);
	}
	
	public void deleteTag(Long id) {
		tagRepo.deleteById(id);
	}
	
}