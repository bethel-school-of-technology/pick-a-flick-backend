package com.pickaflick.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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

import com.pickaflick.exceptions.UnauthorizedException;
import com.pickaflick.models.Movie;
import com.pickaflick.models.Tag;
import com.pickaflick.services.MovieService;
import com.pickaflick.services.TagService;
import com.pickaflick.services.UserService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Autowired
	private final MovieService movieService;

	@Autowired
	private final TagService tagService;

	@Autowired
	private final UserService userService;

	public MovieController(MovieService movieService, TagService tagService, UserService userService) {
		this.movieService = movieService;
		this.tagService = tagService;
		this.userService = userService;
	}

	// Gets all the movies - checks that userId matches authorId first
	// Principal is the current user. 
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovies(Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		Long authorId = currentUserId;
		List<Movie> movies = movieService.findMoviesByAuthorId(authorId);
		return new ResponseEntity<>(movies, HttpStatus.OK);
	}
	
	// returns 10 most recently added movies - checks that userId matches authorId first
	//most recent movies are shown in reverse order to show the latest movie watched. If the user hasn't watched
	//10 movies it will show the entire history starting with the most recent.
	@GetMapping("/recent")
	public ResponseEntity<List<Movie>> getRecentMovies(Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		Long authorId = currentUserId;
		List<Movie> movies = movieService.findMoviesByAuthorId(authorId);
		if(movies.size() > 10) {
			List<Movie> recentMovies = movies.subList(movies.size()-10, movies.size());
			Collections.reverse(recentMovies);
			return new ResponseEntity<>(recentMovies, HttpStatus.OK);
		}
		else {
			Collections.reverse(movies);
			return new ResponseEntity<>(movies, HttpStatus.OK);
		}
	}

	// Gets movie by id - checks that userId matches authorId first
	@GetMapping("/find/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		Movie movie = movieService.findMovieById(id);
		if (movie.getAuthorId() == currentUserId) {
			return new ResponseEntity<>(movie, HttpStatus.OK);
		} else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}
	
	// Gets movies by tag - checks that userId matches authorId first
	@GetMapping("/find/tag/{tagId}")
	public ResponseEntity<List<Movie>> getMoviesByTag(@PathVariable("tagId") Long tagId, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// take the tagId from the route & find that tag
		Tag tag = tagService.findTagById(tagId);
		// get the authorId of that tag
		Long tagAuthorId = tag.getAuthorId();
		// if the tag's authorId doesn't match the currentUserId, throw an error
		if(tagAuthorId != currentUserId) {
			throw new UnauthorizedException("User is not authorized to access.");
		}
		// else, use that tag to find all the movies with that tag
		else {
			// take that tag and use it to find all movies with that tag
			List<Movie> taggedMovies = movieService.findMoviesByTag(tag);
			// create var to hold user's tagged movies (tagged movies that match the author/userId)
			List<Movie> myTaggedMovies = new ArrayList<Movie>();
			// loop through the array list and add every movie with an authorId that matches the current user Id to my newly created var
			for(Movie movie : taggedMovies) {
				if (movie.getAuthorId() == currentUserId) {
					myTaggedMovies.add(movie);
				}
			}
			return new ResponseEntity<>(myTaggedMovies, HttpStatus.OK);
		}
	}

	// Adds new movie - gets userId and assigns it to authorId
	@PostMapping("/add")
	public ResponseEntity<Movie> addMovie(@RequestBody Movie movie, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// sets the authorId to the userId
		movie.setAuthorId(currentUserId);
		// then saves the new movie
		Movie newMovie = movieService.addMovie(movie);
		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}

	// Updates movie - checks that userId matches authorId first
	@PutMapping("/update/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie,
			Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// gets the movie by the id passed in
		Movie movieById = movieService.findMovieById(id);
		// gets the authorId of that movie
		Long movieAuthorId = movieById.getAuthorId();

		if (currentUserId == movieAuthorId) {
			// saves the movie with updated info
			Movie updateMovie = movieService.updateMovie(movie);
			return new ResponseEntity<>(updateMovie, HttpStatus.OK);
		} else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}
	
	// Deletes movie - checks that userId matches authorId first
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteMovie(@PathVariable("id") Long id, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// gets the movie by the id passed in
		Movie movieById = movieService.findMovieById(id);
		// gets the authorId of that movie
		Long movieAuthorId = movieById.getAuthorId();
		
		if(currentUserId == movieAuthorId) {
		 movieService.deleteMovie(id);
		 return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}
}