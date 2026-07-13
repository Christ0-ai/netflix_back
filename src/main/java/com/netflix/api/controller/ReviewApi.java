package com.netflix.api.controller;

import com.netflix.api.controller.advice.ErrorDto;
import com.netflix.api.service.dto.ReviewResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Reviews", description = "Review API")
@RequestMapping("/reviews")
public interface ReviewApi {

  @Operation(summary = "List of reviews")
  @ApiResponse(responseCode = "200", description = "List of reviews")
  @GetMapping
  ResponseEntity<List<ReviewResponseDto>> getReviews();

  @Operation(summary = "Get one review by its id")
  @ApiResponse(responseCode = "200", description = "Review found")
  @ApiResponse(
      responseCode = "404",
      description = "Review not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @GetMapping("/{id}")
  ResponseEntity<ReviewResponseDto> getReviewById(
      @PathVariable @Parameter(description = "Review id", required = true) int id);
}
