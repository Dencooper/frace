package vn.dencooper.fracejob.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.Role;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse.CompanyUserResponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse.RoleUserResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.repository.CompanyRepository;
import vn.dencooper.fracejob.repository.JobRepository;
import vn.dencooper.fracejob.repository.RoleRepository;
import vn.dencooper.fracejob.repository.UserRepository;
import vn.dencooper.fracejob.utils.JwtUtil;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    CompanyRepository companyRepository;
    RoleRepository roleRepository;
    JobRepository jobRepository;

    public UserResponse handleCreateUser(UserCreationRequest request) {
        if (IsExistedUserByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.toUser(request);
        user = userRepository.save(handleCompanyAndRoleUser(user));

        UserResponse res = userMapper.toUserResponse(user);

        if (user.getCompany() != null) {
            res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
        }

        if (user.getRole() != null) {
            res.setRole(new RoleUserResponse(user.getRole().getId(), user.getRole().getName()));
        }

        return res;
    }

    public UserResponse fetchUserById(long id) {
        User user = new User();
        user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse res = userMapper.toUserResponse(user);
        if (user.getCompany() != null) {
            res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
        }

        if (user.getRole() != null) {
            res.setRole(new RoleUserResponse(user.getRole().getId(), user.getRole().getName()));
        }
        return res;
    }

    public PaginationResponse fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec, pageable);

        PaginationResponse paginationResponse = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageUsers.getTotalPages())
                .total(pageUsers.getTotalElements())
                .build();
        paginationResponse.setMeta(meta);
        paginationResponse.setResult(pageUsers
                .getContent()
                .stream()
                .map((user) -> {
                    UserResponse res = userMapper.toUserResponse(user);
                    if (user.getCompany() != null) {
                        res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
                    }

                    if (user.getRole() != null) {
                        res.setRole(new RoleUserResponse(user.getRole().getId(), user.getRole().getName()));
                    }
                    return res;
                })
                .toList());

        return paginationResponse;
    }

    public UserResponse handleUpdateUser(long id, UserUpdationResquest request) {
        User user = new User();
        user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        userMapper.toUser(user, request);
        user = userRepository.save(handleCompanyAndRoleUser(user));

        UserResponse res = userMapper.toUserResponse(user);
        if (user.getCompany() != null) {
            res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
        }

        return res;
    }

    public UserResponse handleUpdateUserLogin(UserUpdationResquest request) {
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        User user = new User();
        user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        userMapper.toUser(user, request);
        user = userRepository.save(handleCompanyAndRoleUser(user));

        UserResponse res = userMapper.toUserResponse(user);
        if (user.getCompany() != null) {
            res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
        }

        return res;
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

    public User handleCompanyAndRoleUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = companyRepository.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        if (user.getRole() != null) {
            Optional<Role> roleOptional = roleRepository.findById(user.getRole().getId());
            user.setRole(roleOptional.isPresent() ? roleOptional.get() : null);
        }

        return user;
    }

    public UserResponse getUserLogin() {
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        User user = new User();
        user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse res = userMapper.toUserResponse(user);
        if (user.getCompany() != null) {
            res.setCompany(new CompanyUserResponse(user.getCompany().getId(), user.getCompany().getName()));
        }

        if (user.getRole() != null) {
            res.setRole(new RoleUserResponse(user.getRole().getId(), user.getRole().getName()));
        }
        return res;
    }

    public ArrayList<Long> count() {
        ArrayList<Long> arr = new ArrayList<>();
        arr.add(userRepository.count());
        arr.add(companyRepository.count());
        arr.add(jobRepository.count());
        return arr;
    }
}
