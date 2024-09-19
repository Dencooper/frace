package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    void updateUser(@MappingTarget User user, UserUpdationResquest request);

    UserResponse toUserResponse(User user);

}
