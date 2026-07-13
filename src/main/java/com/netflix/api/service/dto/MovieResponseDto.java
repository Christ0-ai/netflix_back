package com.netflix.api.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netflix.api.model.enums.EGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public record MovieResponseDto(
    @Schema(description = "Id of the movie", example = "12") int id,
    @Schema(description = "Movie Title", example = "Fast And Furious") String title,
    @Schema(
            description = "Short movie synopsis",
            example =
                "An undercover cop infiltrates the high-speed world of illegal street racing, where loyalty, friendship, and adrenaline blur the line between right and wrong")
        String description,
    @JsonFormat(pattern = "dd-MM-yyyy")
        @Schema(description = "Release date of the movie", format = "date", example = "13-07-2026")
        LocalDate releaseDate,
    @Schema(description = "Genre of the movie", example = "HORROR") EGenre genre,
    @Schema(description = "Url of the movie poster", example = "https://image.org/path.jpg")
        String posterPath,
    @Schema(description = "List of reviews for the movie") List<ReviewResponseDto> reviews) {}
