package com.pickaflick.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pickaflick.models.Movie;
import com.pickaflick.models.Tag;

public interface IMovieRepo extends JpaRepository<Movie, Long> {
	
	@Query
	List<Movie> findByTags(Tag tag);
	
	@Query
	List<Movie> findByAuthorId(Long authorId);

}

