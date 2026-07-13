package com.netflix.api.controller;

import com.netflix.api.controller.advice.ErrorDto;
import com.netflix.api.service.dto.UserResponseDto;
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

@Tag(name = "Users", description = "User API")
@RequestMapping("/users")
public interface UserApi {

  @Operation(summary = "List of users")
  @ApiResponse(responseCode = "200", description = "List of users")
  @GetMapping
  ResponseEntity<List<UserResponseDto>> getUsers();

  @Operation(summary = "Get one user by its id")
  @ApiResponse(responseCode = "200", description = "User found")
  @ApiResponse(
      responseCode = "404",
      description = "User not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @GetMapping("/{id}")
  ResponseEntity<UserResponseDto> getUserById(
      @PathVariable @Parameter(description = "User id", required = true) int id);
}
