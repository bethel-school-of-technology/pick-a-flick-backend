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
	
	public List<Movie> findMoviesByAuthorId(Long id) {
		return movieRepo.findByAuthorId(id);
	}
	
	public Movie addMovie(Movie movie) {
		return movieRepo.save(movie);
	}
	
	public Movie updateMovie(Movie movie) {
		return movieRepo.save(movie);
	}
	
	public void deleteMovie(Long id) {
		movieRepo.deleteById(id);
	}

}