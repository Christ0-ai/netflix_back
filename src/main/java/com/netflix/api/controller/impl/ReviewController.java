package com.netflix.api.controller.impl;

import com.netflix.api.controller.ReviewApi;
import com.netflix.api.service.ReviewService;
import com.netflix.api.service.dto.ReviewResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class ReviewController implements ReviewApi {

  private ReviewService reviewService;

  @Override
  public ResponseEntity<List<ReviewResponseDto>> getReviews() {
    log.info("Access to endpoint GET/Reviews");
    return ResponseEntity.ok(reviewService.findAll());
  }

  @Override
  public ResponseEntity<ReviewResponseDto> getReviewById(int id) {
    log.info("Access to endpoint GET/reviews/{id}");
    return ResponseEntity.ok(reviewService.findById(id));
  }
}
