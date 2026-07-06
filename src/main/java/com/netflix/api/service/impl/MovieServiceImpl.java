package com.netflix.api.service.impl;

import com.netflix.api.mapper.MovieMapper;
import com.netflix.api.model.Movie;
import com.netflix.api.repository.MovieRepository;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {

    private static final String MOVIE_NOT_FOUND = "movie.id.not_found";

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MessageSourceAccessor messages;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> findAll() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::toMovieResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponseDto findById(int id) {
        Optional<Movie> optMovie = movieRepository.findById(id);
        if (optMovie.isEmpty())
            throw new EntityNotFoundException(messages.getMessage(MOVIE_NOT_FOUND));

        return movieMapper.toMovieResponseDto(optMovie.get());
    }
}
