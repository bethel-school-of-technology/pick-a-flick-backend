package com.pickaflick.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pickaflick.models.Movie;

public interface IMovieRepo extends JpaRepository<Movie, Long> {

}

