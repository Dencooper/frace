package vn.dencooper.fracejob.controller;

import java.util.ArrayList;

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
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    PasswordEncoder passwordEncoder;

    @PostMapping
    @ApiMessage("Create User")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreationRequest req) {
        String hashPassword = passwordEncoder.encode(req.getPassword());
        req.setPassword(hashPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.handleCreateUser(req));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch User By ID")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(userService.fetchUserById(id));
    }

    @GetMapping("/user-login")
    @ApiMessage("Fetch User Login")
    public ResponseEntity<UserResponse> getUserLogin() {
        return ResponseEntity.ok().body(userService.getUserLogin());
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
        return ResponseEntity.ok().body(userService.handleUpdateUser(id, request));
    }

    @PutMapping("/user-login")
    @ApiMessage("Update User")
    public ResponseEntity<UserResponse> updateUserLogin(@Valid @RequestBody UserUpdationResquest request) {
        return ResponseEntity.ok().body(userService.handleUpdateUserLogin(request));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/quantity")
    @ApiMessage("Get quantity user")
    public ArrayList<Long> getQuantity() {
        return userService.count();
    }

}
