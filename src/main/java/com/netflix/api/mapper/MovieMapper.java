package com.netflix.api.mapper;

import com.netflix.api.model.Movie;
import com.netflix.api.service.dto.MovieRequestDto;
import com.netflix.api.service.dto.MovieResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {
  MovieResponseDto toMovieResponseDto(Movie movie);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  Movie toMovie(MovieRequestDto requestDto);
}
