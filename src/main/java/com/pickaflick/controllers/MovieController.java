package com.pickaflick.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickaflick.models.Movie;
import com.pickaflick.models.Tag;
import com.pickaflick.services.MovieService;
import com.pickaflick.services.TagService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private final MovieService movieService;
	
	@Autowired
	private final TagService tagService;

	public MovieController(MovieService movieService, TagService tagService) {
		this.movieService = movieService;
		this.tagService = tagService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovies() {
		 List<Movie> movies = movieService.findAllMovies();
		 return new ResponseEntity<>(movies, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
		 Movie movie = movieService.findMovieById(id);
		 return new ResponseEntity<>(movie, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
		Movie newMovie = movieService.addMovie(movie);
		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
		 Movie updateMovie = movieService.updateMovie(movie);
		 return new ResponseEntity<>(updateMovie, HttpStatus.OK);
	}
	
	// For Many to Many Relationship - attaches tag to movie
//	@PutMapping("/{movieId}/tags/{tagId}")
//	public ResponseEntity<Movie> attachTagToMovie(@PathVariable("movieId") Long movieId, @PathVariable("tagId") Long tagId) {
//		Movie movie = movieService.findMovieById(movieId);
//		Tag tag = tagService.findTagById(tagId);
//		movie.attachTag(tag);
//		Movie taggedMovie = movieService.addMovie(movie);
//		return new ResponseEntity<>(taggedMovie, HttpStatus.OK);
//	}
//	
//	@PutMapping("/{movieId}/tags/remove/{tagId}")
//	public ResponseEntity<Movie> removeTagFromMovie(@PathVariable("movieId") Long movieId, @PathVariable("tagId") Long tagId) {
//		Movie movie = movieService.findMovieById(movieId);
//		Tag tag = tagService.findTagById(tagId);
//		movie.detachTag(tag);
//		Movie untaggedMovie = movieService.addMovie(movie);
//		return new ResponseEntity<>(untaggedMovie, HttpStatus.OK);
//	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteMovie(@PathVariable("id") Long id) {
		movieService.deleteMovie(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
}