package com.netflix.api.service.dto;

import com.netflix.api.model.enums.EGenre;
import com.netflix.api.utils.Messages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record MovieRequestDto(
    @Schema(description = "Movie Title", example = "The Drama")
        @NotBlank(message = Messages.MOVIE_TITLE_NULL)
        String title,
    @Schema(
            description = "Short movie synopsis",
            example =
                "A happily engaged couple's relationship is thrown into turmoil when an unexpected revelation surfaces just days before their wedding, forcing them to confront secrets, trust issues, and the true nature of their love.")
        @NotBlank(message = Messages.MOVIE_DESCRIPTION_NULL)
        String description,
    @Schema(description = "Release date of the movie", format = "date", example = "2026-04-03")
        @NotNull(message = Messages.MOVIE_RELEASE_DATE_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate releaseDate,
    @Schema(description = "Genre of the movie", example = "DRAMA")
        @NotNull(message = Messages.MOVIE_GENRE_NULL)
        EGenre genre,
    @Schema(
            description = "Url of the movie poster",
            example = "https://image.tmdb.org/t/p/w500/1oKLEA9JOhvaBwLpqjROisvWMy7.jpg")
        @NotBlank(message = Messages.MOVIE_POSTER_PATH_NULL)
        @Pattern(
            regexp = "^(https?://).+\\.(jpg|jpeg|png|webp)$",
            message = Messages.MOVIE_POSTER_PATH_INVALID)
        String posterPath) {}
