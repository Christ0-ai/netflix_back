package com.netflix.api.service;

import com.netflix.api.service.dto.ReviewResponseDto;
import java.util.List;

public interface ReviewService {
  List<ReviewResponseDto> findAll();

  ReviewResponseDto findById(int id);
}
