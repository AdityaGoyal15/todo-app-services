package org.maverick.user;

import org.maverick.user.dto.CreateUserDto;
import org.maverick.user.dto.UpdateUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PostMapping
  public ResponseEntity<User> save(@RequestBody CreateUserDto requestDto) {
    return new ResponseEntity<>(userService.save(requestDto), HttpStatus.CREATED);
  }

  @PutMapping("{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UpdateUserDto requestDto) {
    return ResponseEntity.ok(userService.update(id, requestDto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<User> delete(@PathVariable Long id) {
    return ResponseEntity.ok(userService.delete(id));
  }
}
