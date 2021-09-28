package com.pickaflick.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.Movie;
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
	
	public List<Tag> findTagsByAuthorId(Long id) {
		return tagRepo.findByAuthorId(id);
	}
	
	public Tag addTag(Tag tag) {
		return tagRepo.save(tag);
	}
	
	public Tag updateTag(Tag tag) {
		return tagRepo.save(tag);
	}
	
	public void deleteTag(Long id) {
		tagRepo.deleteById(id);
	}
	
}