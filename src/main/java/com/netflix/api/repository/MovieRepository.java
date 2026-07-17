package com.netflix.api.repository;

import com.netflix.api.model.Movie;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

  Optional<Movie> findByTitle(String title);

  boolean existsByTitle(String title);

  boolean existsByReleaseDate(String releaseDate);

  boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);
}
