package vn.dencooper.fracejob.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.user.CompanyUserResponse;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.CompanyMapper;
import vn.dencooper.fracejob.mapper.UserMapper;
import vn.dencooper.fracejob.repository.CompanyRepository;
import vn.dencooper.fracejob.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;

    public User handleCreateUser(UserCreationRequest request) {
        if (IsExistedUserByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        // if (request.getCompany() != null) {
        // Optional<Company> companyOptional =
        // companyRepository.findById(request.getCompany().getId());
        // request.setCompany(companyOptional.isPresent() ?
        // companyMapper.toCompany(companyOptional.get()) : null);
        // }

        return userRepository.save(userMapper.toUser(request));
    }

    public User fetchUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    }

    public PaginationResponse fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec, pageable);

        PaginationResponse paginationResponse = new PaginationResponse();
        Meta meta = Meta.builder()
                .current(pageable.getPageNumber() + 1)
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
                    res.setCompany(handleCompanyUser(user.getCompany()));
                    return res;
                })
                .toList());

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

    public CompanyUserResponse handleCompanyUser(Company company) {
        if (company != null) {
            Optional<Company> companyOptional = companyRepository.findById(company.getId());
            CompanyUserResponse companyUser = companyOptional.isPresent()
                    ? companyMapper.toCompanyUser(companyOptional.get())
                    : null;
            return companyUser;
        }
        return null;
    }
}
