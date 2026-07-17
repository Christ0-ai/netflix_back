package com.netflix.api.service.impl;

import com.netflix.api.mapper.ReviewMapper;
import com.netflix.api.model.Review;
import com.netflix.api.repository.ReviewRepository;
import com.netflix.api.service.ReviewService;
import com.netflix.api.service.dto.ReviewResponseDto;
import com.netflix.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  @Override
  @Transactional(readOnly = true)
  public List<ReviewResponseDto> findAll() {
    List<Review> reviews = reviewRepository.findAll();
    return reviews.stream().map(reviewMapper::toReviewResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewResponseDto findById(int id) {
    Review review =
        reviewRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Messages.REVIEW_ID_NOT_FOUND));
    return reviewMapper.toReviewResponseDto(review);
  }
}
