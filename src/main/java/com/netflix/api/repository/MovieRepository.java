package com.netflix.api.repository;

import com.netflix.api.model.Movie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

  Optional<Movie> findByTitle(String title);
}
