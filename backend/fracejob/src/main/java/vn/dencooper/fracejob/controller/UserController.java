package vn.dencooper.fracejob.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.UserResponse;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @PostMapping
    @ApiMessage("Create User")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(hashPassword);
        User user = userService.handleCreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(user));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch User By ID")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") long id) {
        User user = userService.fetchUserById(id);
        return ResponseEntity.ok().body(userMapper.toUserResponse(user));
    }

    @GetMapping
    @ApiMessage("Fetch All Users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.fetchAllUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toListUsersResponse(users));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update User")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") long id,
            @Valid @RequestBody UserUpdationResquest request) {
        User user = userService.handleUpdateUser(id, request);
        return ResponseEntity.ok().body(userMapper.toUserResponse(user));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        User user = userService.fetchUserById(id);
        userService.handleDeleteUser(user);
        return ResponseEntity.ok().body(null);
    }

}
