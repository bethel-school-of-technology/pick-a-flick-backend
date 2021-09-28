package com.pickaflick.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pickaflick.models.Tag;

public interface ITagRepo extends JpaRepository<Tag, Long> {
	
	@Query
	List<Tag> findByAuthorId(Long authorId);

}
