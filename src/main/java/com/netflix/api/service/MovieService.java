package com.netflix.api.service;

import com.netflix.api.service.dto.MovieRequestDto;
import com.netflix.api.service.dto.MovieResponseDto;
import java.util.List;

public interface MovieService {
  List<MovieResponseDto> findAll();

  MovieResponseDto findById(int id);

  MovieResponseDto findByTitle(String title);

  MovieResponseDto addMovie(MovieRequestDto requestDto);

  void deleteMovie(int id);
}
