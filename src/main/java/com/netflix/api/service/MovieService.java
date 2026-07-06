package com.netflix.api.service;

import com.netflix.api.service.dto.MovieResponseDto;
import java.util.List;

public interface MovieService {
  List<MovieResponseDto> findAll();

  MovieResponseDto findById(int id);
}
