package com.netflix.api.service.impl;

import static org.mockito.Mockito.mock;

import com.netflix.api.mapper.UserMapper;
import com.netflix.api.model.User;
import com.netflix.api.model.enums.ERole;
import com.netflix.api.repository.UserRepository;
import com.netflix.api.service.UserService;
import com.netflix.api.service.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @Mock private MessageSourceAccessor messages;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userMapper = mock(UserMapper.class);
    messages = mock(MessageSourceAccessor.class);
    userService = new UserServiceImpl(userRepository, userMapper, messages);
  }

  @Nested
  @DisplayName("getUsers Tests")
  class GetUsersTests {
    @Test
    @DisplayName("findAll(), should find all users and return UserResponseDto list with success")
    void findAllUsersValidTest() {
      User user1 = new User(1, "Jane Doe", "jane.doe@example.com", "password", ERole.USER);

      User user2 = new User(2, "John Doe", "john.doe@example.com", "password", ERole.USER);

      UserResponseDto expected1 =
          new UserResponseDto(1, "Jane Doe", "jane.doe@example.com", ERole.USER);

      UserResponseDto expected2 =
          new UserResponseDto(2, "John Doe", "john.doe@example.com", ERole.USER);

      Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));
      Mockito.when(userMapper.toUserResponseDto(user1)).thenReturn(expected1);
      Mockito.when(userMapper.toUserResponseDto(user2)).thenReturn(expected2);

      List<UserResponseDto> response = userService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () ->
              Assertions.assertEquals(
                  2, response.size(), "Response list size should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  expected1, response.getFirst(), "First DTO should match expected user"),
          () ->
              Assertions.assertEquals(
                  expected2, response.getLast(), "Last DTO should match expected user"));

      Mockito.verify(userRepository).findAll();
      Mockito.verify(userMapper, Mockito.times(2)).toUserResponseDto(Mockito.any());
    }

    @Test
    @DisplayName("findAll(), should return an empty list when no user exists")
    void findAllUsersEmptyListTest() {

      Mockito.when(userRepository.findAll()).thenReturn(List.of());

      List<UserResponseDto> response = userService.findAll();

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response list should not be null"),
          () -> Assertions.assertTrue(response.isEmpty(), "Response list should be empty"));

      Mockito.verify(userRepository).findAll();
      Mockito.verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("findById() should find a user and return UserResponseDto with success")
    void findByIdValidTest() {

      int id = 1;

      User user = new User(id, "Jane Doe", "jane.doe@example.com", "password", ERole.USER);

      UserResponseDto expected =
          new UserResponseDto(id, "Jane Doe", "jane.doe@example.com", ERole.USER);

      Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
      Mockito.when(userMapper.toUserResponseDto(user)).thenReturn(expected);

      UserResponseDto response = userService.findById(id);

      Assertions.assertAll(
          () -> Assertions.assertNotNull(response, "Response should not be null"),
          () -> Assertions.assertEquals(id, response.id(), "Id should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "Jane Doe", response.name(), "Name should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  "jane.doe@example.com", response.email(), "Email should be the same as expected"),
          () ->
              Assertions.assertEquals(
                  ERole.USER, response.role(), "Role should be the same as expected"));

      Mockito.verify(userRepository).findById(id);
      Mockito.verify(userMapper).toUserResponseDto(user);
    }

    @Test
    @DisplayName("findById(), should thrown EntityNotFoundException when id not found")
    void findByIdFailureIdNotFound() {

      int id = 99;

      Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
      Mockito.when(messages.getMessage("user.id.not_found")).thenReturn("User not found");

      Assertions.assertThrows(EntityNotFoundException.class, () -> userService.findById(id));

      Mockito.verify(userRepository).findById(id);
      Mockito.verifyNoInteractions(userMapper);
    }
  }
}
