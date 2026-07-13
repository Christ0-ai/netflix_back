package com.netflix.api.mapper;

import com.netflix.api.model.Review;
import com.netflix.api.service.dto.ReviewResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
  ReviewResponseDto toReviewResponseDto(Review review);
}
