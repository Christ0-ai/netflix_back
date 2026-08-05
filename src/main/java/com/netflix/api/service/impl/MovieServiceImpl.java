package com.netflix.api.service.impl;

import com.netflix.api.exception.MovieDuplicateException;
import com.netflix.api.mapper.MovieMapper;
import com.netflix.api.model.Movie;
import com.netflix.api.repository.MovieRepository;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieRequestDto;
import com.netflix.api.service.dto.MovieResponseDto;
import com.netflix.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MovieServiceImpl implements MovieService {

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final MessageSourceAccessor messages;

  @Override
  @Transactional(readOnly = true)
  public List<MovieResponseDto> findAll() {
    List<Movie> movies = movieRepository.findAll();
    return movies.stream().map(movieMapper::toMovieResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public MovieResponseDto findById(int id) {
    Movie movie =
        movieRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Messages.MOVIE_ID_NOT_FOUND));

    return movieMapper.toMovieResponseDto(movie);
  }

  @Override
  @Transactional(readOnly = true)
  public MovieResponseDto findByTitle(String title) {

    Movie movie =
        movieRepository
            .findByTitle(title)
            .orElseThrow(() -> new EntityNotFoundException(Messages.MOVIE_TITLE_NOT_FOUND));

    return movieMapper.toMovieResponseDto(movie);
  }

  @Override
  public MovieResponseDto addMovie(MovieRequestDto requestDto) {

    validateMovieNotDuplicate(requestDto);

    Movie saved = movieRepository.save(movieMapper.toMovie(requestDto));

    log.info("Movie created: id={}, title={}", saved.getId(), saved.getTitle());
    return movieMapper.toMovieResponseDto(saved);
  }

  @Override
  public void deleteMovie(int id) {
    Movie movie =
        movieRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Messages.MOVIE_ID_NOT_FOUND));

    log.info("Movie to delete: id={}, title={}", movie.getId(), movie.getTitle());
    movieRepository.delete(movie);
  }

  private void validateMovieNotDuplicate(MovieRequestDto requestDto) {

    if (movieRepository.existsByTitleAndReleaseDate(requestDto.title(), requestDto.releaseDate()))
      throw new MovieDuplicateException(messages.getMessage(Messages.MOVIE_ALREADY_EXISTS));
  }
}
