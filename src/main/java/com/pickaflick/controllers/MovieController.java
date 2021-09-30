package com.pickaflick.controllers;

import java.security.Principal;
import java.util.ArrayList;
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

//	@GetMapping("/all")
//	public ResponseEntity<List<Movie>> getAllMovies() {
//		 List<Movie> movies = movieService.findAllMovies();
//		 return new ResponseEntity<>(movies, HttpStatus.OK);
//	}

	// checks that userId matches authorId first
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovies(Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		Long authorId = currentUserId;
		List<Movie> movies = movieService.findMoviesByAuthorId(authorId);
		return new ResponseEntity<>(movies, HttpStatus.OK);
	}

//	@GetMapping("/find/{id}")
//	public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
//		 Movie movie = movieService.findMovieById(id);
//		 return new ResponseEntity<>(movie, HttpStatus.OK);
//	}

	// checks that userId matches authorId first
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

//	@GetMapping("/find/tags?tagId={tagId}")   <- this doesn't work...was hoping everything after the ? would be optional but it errors.
//	@GetMapping("/find/tag/{tagId}")
//	public ResponseEntity<List<Movie>> getMoviesByTag(@PathVariable("tagId") Long tagId) {
//		// take the tagId from the route & find that tag
//		Tag tag = tagService.findTagById(tagId);
//		// take that tag and use it to find all movies with that tag
//		List<Movie> movies = movieService.findMoviesByTag(tag);
//		return new ResponseEntity<>(movies, HttpStatus.OK);
//	}
	
	// checks that userId matches authorId first
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
			ArrayList<Movie> myTaggedMovies = new ArrayList<Movie>();
			// loop through the array list and add every movie with an authorId that matches the current user Id to my newly created var
			for(Movie movie : taggedMovies) {
				if (movie.getAuthorId() == currentUserId) {
					myTaggedMovies.add(movie);
				}
			}
			return new ResponseEntity<>(myTaggedMovies, HttpStatus.OK);
		}
	}
	
	// ...another approach that I was trying for the above method:
//	@GetMapping("/find/tag/{tagId}")
//	public ResponseEntity<List<Movie>> getMoviesByTag(@PathVariable("tagId") Long tagId, Principal principal) {
//		// Get the current user's id, which is the authorId for all their movies
//		Long currentUserId = userService.getUserIdFromPrincipal(principal);
//		
//		// Get all the movies with that authorId
//		Long authorId = currentUserId;
//		List<Movie> allMovies = movieService.findMoviesByAuthorId(authorId);
//		
//		// make allMovies into an array list
//		
//		
//		// take the tagId from the route & find that tag
//		Tag tag = tagService.findTagById(tagId);
//		
//		// take that tag and use it to find all movies with that tag...how to make this method run on just allMovies instead of the movieRepo?  Is this possible?
//		List<Movie> taggedMovies = allMovies.movieService.findMoviesByTag(tag);
//		
//		ArrayList<Movie> moviesArray = new ArrayList<Movie>(movies);
//		for(int i = 0; i < moviesArray.size(); i++) {
//			Array<Movie> taggedMovies;
//			if (moviesArray[i].authorId == currentUserId) {
//				taggedMovies.push(moviesArray[i]);
//			}
//			return taggedMovies;
//		}
//		
//		return new ResponseEntity<>(movies, HttpStatus.OK);
//	}

	@PostMapping("/add")
	public ResponseEntity<Movie> addMovie(@RequestBody Movie movie, Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// sets the authorId to the userId
		movie.setAuthorId(currentUserId);
		// then saves the new movie
		Movie newMovie = movieService.addMovie(movie);
		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}

//	@PutMapping("/update/{id}")
//	public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie, Principal principal) {
//		Long currentUserId = userService.getUserIdFromPrincipal(principal);
//		// sets the authorId to the userId
//		movie.setAuthorId(currentUserId);
//		// then saves the movie with updated info
//		Movie updateMovie = movieService.updateMovie(movie);
//		return new ResponseEntity<>(updateMovie, HttpStatus.OK);
//	}

	// checks that userId matches authorId first
	@PutMapping("/update/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie,
			Principal principal) {
		Long currentUserId = userService.getUserIdFromPrincipal(principal);
		// gets the movie by the id passed in
		Movie movieById = movieService.findMovieById(id);
		// gets the authorId of that movie
		Long movieAuthorId = movieById.getAuthorId();

		if (currentUserId == movieAuthorId) {
			// sets the authorId to the userId
			movie.setAuthorId(currentUserId);
			// then saves the movie with updated info
			Movie updateMovie = movieService.updateMovie(movie);
			return new ResponseEntity<>(updateMovie, HttpStatus.OK);
		} else {
			throw new UnauthorizedException("User is not authorized to access.");
		}
	}

	// For Many to Many Relationship - attaches tag to movie, not using anymore
//	@PutMapping("/{movieId}/tags/{tagId}")
//	public ResponseEntity<Movie> attachTagToMovie(@PathVariable("movieId") Long movieId, @PathVariable("tagId") Long tagId) {
//		Movie movie = movieService.findMovieById(movieId);
//		Tag tag = tagService.findTagById(tagId);
//		movie.attachTag(tag);
//		Movie taggedMovie = movieService.addMovie(movie);
//		return new ResponseEntity<>(taggedMovie, HttpStatus.OK);
//	}
	
	// For Many to Many Relationship - removes tag from movie, not using anymore
//	@PutMapping("/{movieId}/tags/remove/{tagId}")
//	public ResponseEntity<Movie> removeTagFromMovie(@PathVariable("movieId") Long movieId, @PathVariable("tagId") Long tagId) {
//		Movie movie = movieService.findMovieById(movieId);
//		Tag tag = tagService.findTagById(tagId);
//		movie.detachTag(tag);
//		Movie untaggedMovie = movieService.addMovie(movie);
//		return new ResponseEntity<>(untaggedMovie, HttpStatus.OK);
//	}

//	@DeleteMapping("/delete/{id}")
//	public ResponseEntity<?> deleteMovie(@PathVariable("id") Long id) {
//		movieService.deleteMovie(id);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
	
	// checks that userId matches authorId first
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