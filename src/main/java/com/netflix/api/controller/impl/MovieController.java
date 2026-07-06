package com.netflix.api.controller.impl;

import com.netflix.api.controller.MovieApi;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class MovieController implements MovieApi {

  private final MovieService movieService;

  @Override
  public ResponseEntity<List<MovieResponseDto>> getMovies() {
    log.info("Access to endpoint GET/movies");
    return ResponseEntity.ok(movieService.findAll());
  }

  @Override
  public ResponseEntity<MovieResponseDto> getMovieById(int id) {
    log.info("Access to endpoint GET/movies/{id}");
    return ResponseEntity.ok(movieService.findById(id));
  }

  @Override
  public ResponseEntity<MovieResponseDto> findByTitle(String title) {
    log.info("Access to endpoint GET/movies/{title}");
    return ResponseEntity.ok(movieService.findByTitle(title));
  }
}
