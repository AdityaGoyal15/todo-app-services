package org.maverick.user.impl;

import jakarta.transaction.Transactional;
import org.maverick.user.User;
import org.maverick.user.UserRepository;
import org.maverick.user.UserService;
import org.maverick.user.dto.CreateUserDto;
import org.maverick.user.dto.UpdateUserDto;
import org.maverick.user.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User findById(Long id) {
    return findByIdOrElseThrow(id);
  }

  @Override
  @Transactional
  public User save(CreateUserDto requestDto) {
    String email = requestDto.email();
    User user = findByEmailOrElseNull(email);

    if (user != null) {
      throw new IllegalArgumentException(
          "A user with the email [%s] already exists".formatted(email));
    }
    user = new User();
    user.setEmail(email);
    user.setFirstName(requestDto.firstName());
    user.setLastName(requestDto.lastName());
    return userRepository.save(user);
  }

  private User findByEmailOrElseNull(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  @Override
  @Transactional
  public User update(Long id, UpdateUserDto requestDto) {
    User user = findByIdOrElseThrow(id);
    user.setFirstName(requestDto.firstName());
    user.setLastName(requestDto.lastName());
    user.setEmail(requestDto.email());
    user.setImageUrl(requestDto.imageUrl());
    return userRepository.save(user);
  }

  private User findByIdOrElseThrow(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "A user with the ID [%d] does not exist".formatted(id)));
  }

  @Override
  @Transactional
  public User delete(Long id) {
    User user = findByIdOrElseThrow(id);
    userRepository.deleteById(id);
    return user;
  }
}
