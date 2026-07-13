package com.netflix.api.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ReviewResponseDto(
    @Schema(description = "Id of the review", example = "8") int id,
    @Schema(description = "Rating of the movie", example = "3") int rating,
    @Schema(
            description = "Comment explaining the rating",
            example = "A captivating film with excellent visuals.")
        String comment,
    @JsonFormat(pattern = "dd-MM-yyyy")
        @Schema(
            description = "Creation date of the review",
            format = "date",
            example = "13-07-2026")
        LocalDate creationDate,
    @Schema(description = "User who submitted the review") UserResponseDto user) {}
