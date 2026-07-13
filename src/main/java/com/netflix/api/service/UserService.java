package com.netflix.api.service;

import com.netflix.api.service.dto.UserResponseDto;
import java.util.List;

public interface UserService {
  List<UserResponseDto> findAll();

  UserResponseDto findById(int id);
}
