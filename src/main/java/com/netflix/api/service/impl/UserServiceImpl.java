package com.netflix.api.service.impl;

import com.netflix.api.mapper.UserMapper;
import com.netflix.api.model.User;
import com.netflix.api.repository.UserRepository;
import com.netflix.api.service.UserService;
import com.netflix.api.service.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private static final String USER_ID_NOT_FOUND = "user.id.not_found";
  private static final String USER_NAME_NOT_FOUND = "user.name.not_found";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final MessageSourceAccessor messages;

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDto> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream().map(userMapper::toUserResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDto findById(int id) {
    Optional<User> optUser = userRepository.findById(id);
    if (optUser.isEmpty())
      throw new EntityNotFoundException(messages.getMessage(USER_ID_NOT_FOUND));

    return userMapper.toUserResponseDto(optUser.get());
  }
}
