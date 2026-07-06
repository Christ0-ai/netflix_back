package com.netflix.api.controller.impl;

import com.netflix.api.controller.MovieApi;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MovieController implements MovieApi {

  private final MovieService movieService;

  @Override
  public ResponseEntity<List<MovieResponseDto>> getMovies() {
    return ResponseEntity.ok(movieService.findAll());
  }

  @Override
  public ResponseEntity<MovieResponseDto> getMovieById(int id) {
    return ResponseEntity.ok(movieService.findById(id));
  }
}
