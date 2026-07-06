package com.netflix.api.mapper;

import com.netflix.api.model.Movie;
import com.netflix.api.service.dto.MovieResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieResponseDto toMovieResponseDto(Movie movie);
}
