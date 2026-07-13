package com.netflix.api.mapper;

import com.netflix.api.model.User;
import com.netflix.api.service.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponseDto toUserResponseDto(User user);
}
