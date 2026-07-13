package com.netflix.api.service.impl;

import com.netflix.api.mapper.ReviewMapper;
import com.netflix.api.model.Review;
import com.netflix.api.repository.ReviewRepository;
import com.netflix.api.service.ReviewService;
import com.netflix.api.service.dto.ReviewResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

  private static final String REVIEW_ID_NOT_FOUND = "review.id.not_found";

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;
  private final MessageSourceAccessor messages;

  @Override
  @Transactional(readOnly = true)
  public List<ReviewResponseDto> findAll() {
    List<Review> reviews = reviewRepository.findAll();
    return reviews.stream().map(reviewMapper::toReviewResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewResponseDto findById(int id) {
    Optional<Review> optReview = reviewRepository.findById(id);
    if (optReview.isEmpty())
      throw new EntityNotFoundException(messages.getMessage(REVIEW_ID_NOT_FOUND));

    return reviewMapper.toReviewResponseDto(optReview.get());
  }
}
