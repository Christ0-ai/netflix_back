package com.netflix.api.service.impl;

import static org.mockito.Mockito.*;

import com.netflix.api.mapper.ReviewMapper;
import com.netflix.api.model.Movie;
import com.netflix.api.model.Review;
import com.netflix.api.model.User;
import com.netflix.api.model.enums.EGenre;
import com.netflix.api.model.enums.ERole;
import com.netflix.api.repository.ReviewRepository;
import com.netflix.api.service.ReviewService;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock private ReviewRepository reviewRepository;

  @Mock private ReviewMapper reviewMapper;

  private ReviewService reviewService;

  @BeforeEach
  void setUp() {
    reviewRepository = mock(ReviewRepository.class);
    reviewMapper = mock(ReviewMapper.class);
    reviewService = new ReviewServiceImpl(reviewRepository, reviewMapper);
  }

  @Nested
  @DisplayName("getReviews Tests")
  class GetReviewsTests {

    @Test
    @DisplayName("findAll(), should find all movies and return ReviewResponseDto list with success")
    void findAllReviewsValidTest() {
      Movie movie =
          new Movie(
              200,
              "Movie",
              "description",
              LocalDate.of(2026, Month.JULY, 3),
              EGenre.ANIMATION,
              "https://poster.jpg",
              new ArrayList<>());

      User user = new User(1, "user", "user@email.com", "password", ERole.USER);

      Review review1 =
          new Review(100, 3, "comment", LocalDate.of(2026, Month.JULY, 13), movie, user);

      Review review2 =
          new Review(101, 5, "comment", LocalDate.of(2026, Month.JULY, 12), movie, user);

      ReviewResponseDto expected1 =
          new ReviewResponseDto(
              100,
              3,
              "comment",
              LocalDate.of(2026, Month.JULY, 13),
              new UserResponseDto(1, "user", "user@email.com", ERole.USER));

      ReviewResponseDto expected2 =
          new ReviewResponseDto(
              101,
              5,
              "comment",
              LocalDate.of(2026, Month.JULY, 12),
              new UserResponseDto(1, "user", "user@email.com", ERole.USER));

      when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));
      when(reviewMapper.toReviewResponseDto(review1)).thenReturn(expected1);
      when(reviewMapper.toReviewResponseDto(review2)).thenReturn(expected2);

      List<ReviewResponseDto> response = reviewService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () ->
              Assertions.assertEquals(
                  2, response.size(), "Response list size should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  expected1, response.getFirst(), "First DTO should match expected review"),
          () ->
              Assertions.assertEquals(
                  expected2, response.getLast(), "Last DTO should match expected review"));

      verify(reviewRepository).findAll();
      verify(reviewMapper, times(2)).toReviewResponseDto(any());
    }

    @Test
    @DisplayName("findAll(), should return an empty list when no review exists")
    void findAllReviewsEmptyListTest() {
      when(reviewRepository.findAll()).thenReturn(List.of());

      List<ReviewResponseDto> response = reviewService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () -> Assertions.assertTrue(response.isEmpty(), "Response list should be empty"));

      verify(reviewRepository).findAll();
      verifyNoInteractions(reviewMapper);
    }

    @Test
    @DisplayName("findById() should find a review and return ReviewResponseDto with success")
    void findByIdValidTest() {
      int id = 100;

      Movie movie =
          new Movie(
              200,
              "Movie",
              "description",
              LocalDate.of(2026, Month.JULY, 3),
              EGenre.ANIMATION,
              "https://poster.jpg",
              new ArrayList<>());

      User user = new User(1, "user", "user@email.com", "password", ERole.USER);

      Review review = new Review(id, 3, "comment", LocalDate.of(2026, Month.JULY, 13), movie, user);

      ReviewResponseDto expected =
          new ReviewResponseDto(
              id,
              3,
              "comment",
              LocalDate.of(2026, Month.JULY, 13),
              new UserResponseDto(1, "user", "user@email.com", ERole.USER));

      when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
      when(reviewMapper.toReviewResponseDto(review)).thenReturn(expected);

      ReviewResponseDto response = reviewService.findById(id);

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response should not be null"),
          () -> Assertions.assertEquals(id, response.id(), "Id should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  3, response.rating(), "Rating should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "comment", response.comment(), "Comment should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  LocalDate.of(2026, Month.JULY, 13),
                  response.creationDate(),
                  "CreationDate should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  100, response.id(), "Review id should be the same as expected"));

      verify(reviewRepository).findById(id);
      verify(reviewMapper).toReviewResponseDto(review);
    }

    @Test
    @DisplayName("findById(), should thrown EntityNotFoundException when id not found")
    void findByIdFailureIdNotFound() {
      int id = 99;

      when(reviewRepository.findById(id)).thenReturn(Optional.empty());

      Assertions.assertThrows(EntityNotFoundException.class, () -> reviewService.findById(id));

      verify(reviewRepository).findById(id);
      verifyNoInteractions(reviewMapper);
    }
  }
}
