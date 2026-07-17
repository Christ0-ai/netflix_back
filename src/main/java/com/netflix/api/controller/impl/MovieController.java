package com.netflix.api.controller.impl;

import com.netflix.api.controller.MovieApi;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieRequestDto;
import com.netflix.api.service.dto.MovieResponseDto;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
  public ResponseEntity<MovieResponseDto> getByTitle(String title) {
    log.info("Access to endpoint GET/movies/title/{title}");
    return ResponseEntity.ok(movieService.findByTitle(title));
  }

  @Override
  public ResponseEntity<MovieResponseDto> addMovie(MovieRequestDto requestDto) {
    log.info("Access to endpoint POST/movies");

    MovieResponseDto responseDto = movieService.addMovie(requestDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseDto.id())
            .toUri();

    return ResponseEntity.created(location).body(responseDto);
  }
}
