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
import vn.dencooper.fracejob.domain.dto.ApiResponse;
import vn.dencooper.fracejob.domain.dto.request.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.UserResponse;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreationRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(hashPassword);
        User newUser = userService.handleCreateUser(request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userMapper.toUserResponse(newUser));
        apiResponse.setStatusCode(201);
        apiResponse.setMessage("Created User Successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.fetchUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.fetchAllUsers();
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UserUpdationResquest resquest) {
        User updatedUser = userService.handleUpdateUser(resquest);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userMapper.toUserResponse(updatedUser));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Updated User Successfully!");
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) {
        User user = userService.fetchUserById(id);
        userService.handleDeleteUser(user);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Delete User Successfully!");
        return ResponseEntity.ok().body(apiResponse);
    }

}
