package com.netflix.api.service.impl;

import com.netflix.api.mapper.UserMapper;
import com.netflix.api.model.User;
import com.netflix.api.repository.UserRepository;
import com.netflix.api.service.UserService;
import com.netflix.api.service.dto.UserResponseDto;
import com.netflix.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDto> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream().map(userMapper::toUserResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDto findById(int id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Messages.USER_ID_NOT_FOUND));

    return userMapper.toUserResponseDto(user);
  }
}
