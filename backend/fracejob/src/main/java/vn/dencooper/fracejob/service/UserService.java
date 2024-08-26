package vn.dencooper.fracejob.service;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.UserUpdationResquest;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User handleCreateUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        return userRepository.save(user);
    }

    public User fetchUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy User"));
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User handleUpdateUser(UserUpdationResquest request) {
        User user = userMapper.toUser(request);
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(User user) {
        this.userRepository.delete(user);
    }
}
