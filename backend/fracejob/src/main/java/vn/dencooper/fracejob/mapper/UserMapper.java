package vn.dencooper.fracejob.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    void updateUser(@MappingTarget User user, UserUpdationResquest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toListUsersResponse(List<User> users);
}
