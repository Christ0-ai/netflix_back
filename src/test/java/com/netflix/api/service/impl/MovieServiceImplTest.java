package com.netflix.api.service.impl;

import static org.mockito.Mockito.mock;

import com.netflix.api.mapper.MovieMapper;
import com.netflix.api.model.Movie;
import com.netflix.api.repository.MovieRepository;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

  @Mock private MovieRepository movieRepository;

  @Mock private MovieMapper movieMapper;

  @Mock private MessageSourceAccessor messages;

  private MovieService movieService;

  @BeforeEach
  void setUp() {
    movieRepository = mock(MovieRepository.class);
    movieMapper = mock(MovieMapper.class);
    messages = mock(MessageSourceAccessor.class);
    movieService = new MovieServiceImpl(movieRepository, movieMapper, messages);
  }

  @Nested
  @DisplayName("getMovies Tests")
  class getMovies {

    @Test
    @DisplayName("findAll(), should find all movies and return MovieResponseDto list with success")
    void findAllMoviesValidTest() {

      Movie movie1 =
          new Movie(1, "Movie1", "description", LocalDate.of(2026, Month.JULY, 2), 180, "url1.com");
      Movie movie2 =
          new Movie(2, "Movie2", "description", LocalDate.of(2026, Month.JULY, 3), 160, "url2.com");

      MovieResponseDto expected1 =
          new MovieResponseDto(
              1, "Movie1", "description", LocalDate.of(2026, Month.JULY, 2), 180, "url1.com");
      MovieResponseDto expected2 =
          new MovieResponseDto(
              2, "Movie2", "description", LocalDate.of(2026, Month.JULY, 3), 160, "url2.com");

      Mockito.when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
      Mockito.when(movieMapper.toMovieResponseDto(movie1)).thenReturn(expected1);
      Mockito.when(movieMapper.toMovieResponseDto(movie2)).thenReturn(expected2);

      List<MovieResponseDto> response = movieService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () ->
              Assertions.assertEquals(
                  2, response.size(), "Response list size should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  response.getFirst(), expected1, "First DTO should match expected movie"),
          () ->
              Assertions.assertEquals(
                  response.getLast(), expected2, "Last DTO should match expected movie"));

      Mockito.verify(movieRepository).findAll();
      Mockito.verify(movieMapper, Mockito.times(2)).toMovieResponseDto(Mockito.any());
    }

    @Test
    @DisplayName("findAll(), should return an empty list when no movie exists")
    void findAllMoviesEmptyList() {

      Mockito.when(movieRepository.findAll()).thenReturn(List.of());
      List<MovieResponseDto> response = movieService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () -> Assertions.assertTrue(response.isEmpty(), "Response list should be empty"));

      Mockito.verify(movieRepository).findAll();
      Mockito.verifyNoInteractions(movieMapper);
    }

    @Test
    @DisplayName("findById() should find a movie and return MovieResponseDto with success")
    void findMByIdValidTest() {
      int id = 1;
      Movie movie =
          new Movie(
              id, "Movie1", "description", LocalDate.of(2026, Month.JULY, 2), 180, "url1.com");
      MovieResponseDto expected =
          new MovieResponseDto(
              id, "Movie1", "description", LocalDate.of(2026, Month.JULY, 2), 180, "url1.com");

      Mockito.when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
      Mockito.when(movieMapper.toMovieResponseDto(movie)).thenReturn(expected);

      MovieResponseDto response = movieService.findById(id);

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response should not be null"),
          () -> Assertions.assertEquals(id, response.id(), "Id should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "Movie1", response.title(), "Title should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "description",
                  response.description(),
                  "Description should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  LocalDate.of(2026, Month.JULY, 2),
                  response.releaseDate(),
                  "ReleaseDate should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  180, response.duration(), "Duration should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "url1.com", response.url(), "Url should be the same as expected"));

      Mockito.verify(movieRepository).findById(id);
      Mockito.verify(movieMapper).toMovieResponseDto(movie);
    }

    @Test
    @DisplayName("findById(), should thrown EntityNotFoundException when id not found")
    void findByIdFailureIdNotFound() {
      Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.findById(1));
      Mockito.verify(movieRepository).findById(1);
      Mockito.verifyNoInteractions(movieMapper);
    }
  }
}
