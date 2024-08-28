package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.ApiResponse;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.UserResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User handleCreateUser(UserCreationRequest request) {
        if (IsExistedUserByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        return userRepository.save(user);
    }

    public User fetchUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User handleUpdateUser(long id, UserUpdationResquest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        userMapper.updateUser(user, request);
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(User user) {
        this.userRepository.delete(user);
    }

    public boolean IsExistedUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
