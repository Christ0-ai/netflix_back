package com.netflix.api.service.impl;

import static org.mockito.Mockito.mock;

import com.netflix.api.mapper.MovieMapper;
import com.netflix.api.model.Movie;
import com.netflix.api.model.Review;
import com.netflix.api.model.User;
import com.netflix.api.model.enums.EGenre;
import com.netflix.api.model.enums.ERole;
import com.netflix.api.repository.MovieRepository;
import com.netflix.api.service.MovieService;
import com.netflix.api.service.dto.MovieResponseDto;
import com.netflix.api.service.dto.ReviewResponseDto;
import com.netflix.api.service.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
  class getMoviesTests {

    @Test
    @DisplayName("findAll(), should find all movies and return MovieResponseDto list with success")
    void findAllMoviesValidTest() {
      LocalDate creationDate = LocalDate.of(2026, Month.JULY, 13);

      Movie movie1 =
          new Movie(
              11,
              "Movie1",
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              new ArrayList<>());

      Movie movie2 =
          new Movie(
              22,
              "Movie2",
              "description",
              LocalDate.of(2026, Month.JULY, 3),
              EGenre.ANIMATION,
              "https://poster.jpg",
              new ArrayList<>());

      User user1 = new User(1, "user", "user@email.com", "password", ERole.USER);
      Review review1 = new Review(100, 3, "comment", creationDate, movie1, user1);

      ReviewResponseDto reviewResponseDto =
          new ReviewResponseDto(
              100,
              3,
              "comment",
              creationDate,
              new UserResponseDto(1, "user", "user@email.com", ERole.USER));

      movie1.getReviews().add(review1);

      MovieResponseDto expected1 =
          new MovieResponseDto(
              11,
              "Movie1",
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              List.of(reviewResponseDto));
      MovieResponseDto expected2 =
          new MovieResponseDto(
              22,
              "Movie2",
              "description",
              LocalDate.of(2026, Month.JULY, 3),
              EGenre.ANIMATION,
              "https://poster.jpg",
              new ArrayList<>());

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
                  response.getLast(), expected2, "Last DTO should match expected movie"),
          () -> Assertions.assertEquals(1, response.getFirst().reviews().size()),
          () -> Assertions.assertTrue(response.getLast().reviews().isEmpty()));

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
      LocalDate creationDate = LocalDate.of(2026, Month.JULY, 13);

      int id = 11;
      Movie movie =
          new Movie(
              id,
              "Movie1",
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              new ArrayList<>());

      User user1 = new User(1, "user", "user@email.com", "password", ERole.USER);
      Review review1 = new Review(100, 3, "comment", creationDate, movie, user1);

      ReviewResponseDto reviewResponseDto =
          new ReviewResponseDto(
              100,
              3,
              "comment",
              creationDate,
              new UserResponseDto(1, "user", "user@email.com", ERole.USER));

      movie.getReviews().add(review1);

      MovieResponseDto expected =
          new MovieResponseDto(
              id,
              "Movie1",
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              List.of(reviewResponseDto));

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
                  EGenre.ACTION, response.genre(), "Genre should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "https://poster.jpg",
                  response.posterPath(),
                  "Poster path should be the same as expected"),
          () -> Assertions.assertNotNull(response.reviews(), "Reviews should not be null"),
          () ->
              Assertions.assertEquals(
                  1, response.reviews().size(), "Reviews should be the same as expected"));

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

    @Test
    @DisplayName("findByTitle() should find a movie and return MovieResponseDto with success")
    void findMByTitleValidTest() {
      LocalDate creationDate = LocalDate.of(2026, Month.JULY, 13);

      String title = "Movie1";
      int id = 11;
      Movie movie =
          new Movie(
              id,
              title,
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              new ArrayList<>());

      MovieResponseDto expected =
          new MovieResponseDto(
              id,
              title,
              "description",
              LocalDate.of(2026, Month.JULY, 2),
              EGenre.ACTION,
              "https://poster.jpg",
              new ArrayList<>());

      Mockito.when(movieRepository.findByTitle(title)).thenReturn(Optional.of(movie));
      Mockito.when(movieMapper.toMovieResponseDto(movie)).thenReturn(expected);

      MovieResponseDto response = movieService.findByTitle(title);

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response should not be null"),
          () -> Assertions.assertEquals(id, response.id(), "Id should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  title, response.title(), "Title should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "description",
                  response.description(),
                  "Description should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  LocalDate.of(2026, Month.JULY, 2),
                  response.releaseDate(),
                  "ReleaseDate should be the same as expected"));

      Mockito.verify(movieRepository).findByTitle(title);
      Mockito.verify(movieMapper).toMovieResponseDto(movie);
    }

    @Test
    @DisplayName("findByTitle(), should thrown EntityNotFoundException when title not found")
    void findByTitleFailureIdNotFound() {
      String title = "Movie1";
      Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.findByTitle(title));
      Mockito.verify(movieRepository).findByTitle(title);
      Mockito.verifyNoInteractions(movieMapper);
    }
  }
}
