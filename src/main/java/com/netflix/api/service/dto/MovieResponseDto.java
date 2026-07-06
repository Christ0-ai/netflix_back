package com.netflix.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MovieResponseDto(
    @Schema(description = "Id of the movie", example = "1") int id,
    @Schema(description = "Movie Title", example = "Fast And Furious") String title,
    @Schema(
            description = "Short movie synopsis",
            example =
                "An undercover cop infiltrates the high-speed world of illegal street racing, where loyalty, friendship, and adrenaline blur the line between right and wrong")
        String description,
    @Schema(description = "Release date of the movie", format = "date") LocalDate releaseDate,
    @Schema(description = "Duration of the movie in minutes", example = "180") int duration,
    @Schema(description = "Url of the movie") String url) {}
