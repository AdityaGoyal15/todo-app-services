package org.maverick.user;

import org.maverick.user.dto.CreateUserDto;
import org.maverick.user.dto.UpdateUserDto;

public interface UserService {

  User findById(Long id);

  User save(CreateUserDto requestDto);

  User update(Long id, UpdateUserDto requestDto);

  User delete(Long id);
}
