package com.netflix.api.controller;

import com.netflix.api.controller.advice.ErrorDto;
import com.netflix.api.service.dto.MovieResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Movies", description = "Movie API")
@RequestMapping("/movies")
public interface MovieApi {

  @Operation(summary = "List of movies")
  @ApiResponse(responseCode = "200", description = "List of movies")
  @GetMapping
  ResponseEntity<List<MovieResponseDto>> getMovies();

  @Operation(summary = "Get one movie by its id")
  @ApiResponse(responseCode = "200", description = "Movie found")
  @ApiResponse(
      responseCode = "404",
      description = "Movie not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @GetMapping("/{id}")
  ResponseEntity<MovieResponseDto> getMovieById(
      @PathVariable @Parameter(description = "Movie id", required = true) int id);

  @Operation(summary = "Get one movie by its name")
  @ApiResponse(responseCode = "200", description = "Movie found")
  @ApiResponse(
      responseCode = "404",
      description = "Movie not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @GetMapping("/title/{title}")
  ResponseEntity<MovieResponseDto> getByTitle(
      @PathVariable @Parameter(description = "Film name not found", required = true) String title);
}
