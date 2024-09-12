package vn.dencooper.fracejob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
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

    public PaginationResponse fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec, pageable);

        if (pageUsers.getTotalElements() == 0) {
            throw new AppException(ErrorCode.USER_NOTFOUND);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPage(pageable.getPageNumber() + 1);
        paginationResponse.setPageSize(pageable.getPageSize());
        paginationResponse.setTotalPages(pageUsers.getTotalPages());
        paginationResponse.setTotalItems(pageUsers.getTotalElements());
        paginationResponse.setResutl(userMapper.toListUsersResponse(pageUsers.getContent()));

        return paginationResponse;
    }

    public User handleUpdateUser(long id, UserUpdationResquest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        userMapper.updateUser(user, request);
        return userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        userRepository.delete(user);
    }

    public boolean IsExistedUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User fetchUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    }

    public void updateRefreshToken(String email, String refreshToken) {
        User user = this.fetchUserByEmail(email);
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
    }

    public User fetchUserByRefreshAndEmail(String refresh_token, String email) {
        return userRepository.findByRefreshTokenAndEmail(refresh_token, email)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));
    }
}
