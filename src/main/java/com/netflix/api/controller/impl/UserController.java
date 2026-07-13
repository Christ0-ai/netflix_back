package com.netflix.api.controller.impl;

import com.netflix.api.controller.UserApi;
import com.netflix.api.service.UserService;
import com.netflix.api.service.dto.UserResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    log.info("Access to endpoint GET/users");
    return ResponseEntity.ok(userService.findAll());
  }

  @Override
  public ResponseEntity<UserResponseDto> getUserById(int id) {
    log.info("Access to endpoint GET/users/{id}");
    return ResponseEntity.ok(userService.findById(id));
  }
}
