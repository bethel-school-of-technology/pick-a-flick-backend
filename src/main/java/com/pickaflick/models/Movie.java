package com.pickaflick.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Movie implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long movieId;
	@Column(nullable = false, updatable = false)
	private Long authorId;
	private String imageUrl;
	private String movieTitle;
	private int year;
	private int runTimeInMinutes;
	private String genre;
	private String leadActors;
	private String description;
	
//	Creates Many to Many relationship between Movies and Tags
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(
		name = "movie_tags",
		joinColumns = @JoinColumn(name = "movieId"),
		inverseJoinColumns = @JoinColumn(name = "tagId")
		)
	private Set<Tag> tags = new HashSet<>();

	public Movie() {

	}
	
	public Movie(Long movieId, Long authorId, String imageUrl, String movieTitle, int year, int runTimeInMinutes,
			String genre, String leadActors, String description) {

		this.movieId = movieId;
		this.authorId = authorId;
		this.imageUrl = imageUrl;
		this.movieTitle = movieTitle;
		this.year = year;
		this.runTimeInMinutes = runTimeInMinutes;
		this.genre = genre;
		this.leadActors = leadActors;
		this.description = description;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRunTimeInMinutes() {
		return runTimeInMinutes;
	}

	public void setRunTimeInMinutes(int runTimeInMinutes) {
		this.runTimeInMinutes = runTimeInMinutes;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLeadActors() {
		return leadActors;
	}

	public void setLeadActors(String leadActors) {
		this.leadActors = leadActors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
//	for Many to Many relationship - links a tag to a movie
	public void attachTag(Tag tag) {
		this.tags.add(tag);
		tag.getMovies().add(this);
	}
	
//	for Many to Many relationship - removes a tag from a movie
	public void detachTag(Tag tag) {
		this.tags.remove(tag);
		tag.getMovies().remove(this);
	}

	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", authorId=" + authorId + ", imageUrl=" + imageUrl + ", movieTitle="
				+ movieTitle + ", year=" + year + ", runTimeInMinutes=" + runTimeInMinutes + ", genre=" + genre
				+ ", leadActors=" + leadActors + ", description=" + description + "]";
	}
		
}

