package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.user.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.user.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.user.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    void toUser(@MappingTarget User user, UserUpdationResquest request);

    @Mapping(source = "company", target = "company", ignore = true)
    @Mapping(source = "role", target = "role", ignore = true)
    UserResponse toUserResponse(User user);
}
