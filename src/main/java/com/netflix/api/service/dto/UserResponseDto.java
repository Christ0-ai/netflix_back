package com.netflix.api.service.dto;

import com.netflix.api.model.enums.ERole;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(
    @Schema(description = "User id", example = "7") int id,
    @Schema(description = "Username", example = "Lola Lou") String name,
    @Schema(description = "User's email", format = "email", example = "lola.lou@exemple.com")
        String email,
    @Schema(description = "Role of the user") ERole role) {}
