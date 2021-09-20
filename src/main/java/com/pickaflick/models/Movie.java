package com.pickaflick.models;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Movie implements Serializable {
	
//	From Jared's original code - not sure if we need to do this or not:
//	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long movieId;
	private String authorId;
	private String imageUrl;
	private String movieTitle;
	private int year;
	private int runTimeInMinutes;
	private String genre;
	private String leadActors;
	private String description;
	
	public Movie() {
		
	}
	
	public Movie(Long movieId, String authorId, String imageUrl, String movieTitle, int year, int runTimeInMinutes,
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
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

	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", authorId=" + authorId + ", imageUrl=" + imageUrl + ", movieTitle="
				+ movieTitle + ", year=" + year + ", runTimeInMinutes=" + runTimeInMinutes + ", genre=" + genre
				+ ", leadActors=" + leadActors + ", description=" + description + "]";
	}
		
}

