package com.pickaflick.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pickaflick.exceptions.NotFoundException;
import com.pickaflick.models.Movie;
import com.pickaflick.models.Tag;
import com.pickaflick.repos.IMovieRepo;

@Service
public class MovieService {
	
	private final IMovieRepo movieRepo;
	
	@Autowired
	public MovieService(IMovieRepo movieRepo) {
		this.movieRepo = movieRepo;
	}
	
	public List<Movie> findAllMovies() {
		return movieRepo.findAll();
	}
	
	public Movie findMovieById(Long id) {
		return movieRepo.findById(id).orElseThrow(() -> new NotFoundException("Movie by id " + id + " was not found."));
	}
	
	public List<Movie> findMoviesByTag(Tag tag) {
		return movieRepo.findByTags(tag);
	}
	
	// Comments from Jared's original code:
	
	// SetMovieId needs to be Long for it to be a many to many. I have used setMovieTitle, 
	// but this may need to be changed.
	// Above comments were for previous code that is commented out below under addMovie() - not sure if we need to do this or not.
	public Movie addMovie(Movie movie) {
		// Don't think we need this, but didn't want to delete just in case...
		// movies.setMovieTitle(UUID.randomUUID().toString());
		return movieRepo.save(movie);
	}
	
	public Movie updateMovie(Movie movie) {
		return movieRepo.save(movie);
	}
	
	public void deleteMovie(Long id) {
		movieRepo.deleteById(id);
	}

}