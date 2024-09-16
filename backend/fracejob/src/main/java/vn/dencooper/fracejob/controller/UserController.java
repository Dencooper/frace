package vn.dencooper.fracejob.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;
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
        UserResponse res = userMapper.toUserResponse(user);
        res.setCompany(userService.handleCompanyUser(user.getCompany()));
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch User By ID")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") long id) {
        User user = userService.fetchUserById(id);
        UserResponse res = userMapper.toUserResponse(user);
        res.setCompany(userService.handleCompanyUser(user.getCompany()));
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    @ApiMessage("Fetch All Users")
    public ResponseEntity<PaginationResponse> getAllUsers(@Filter Specification<User> spec,
            Pageable pageable) {

        PaginationResponse users = userService.fetchAllUsers(spec, pageable);
        return ResponseEntity.ok().body(users);
    }

    @PutMapping("/{id}")
    @ApiMessage("Update User")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") long id,
            @Valid @RequestBody UserUpdationResquest request) {
        User user = userService.handleUpdateUser(id, request);
        UserResponse res = userMapper.toUserResponse(user);
        res.setCompany(userService.handleCompanyUser(user.getCompany()));
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(null);
    }

}
