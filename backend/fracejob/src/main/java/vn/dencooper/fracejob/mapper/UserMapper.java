package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;

import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.request.UserCreationRequest;
import vn.dencooper.fracejob.domain.dto.request.UserUpdationResquest;
import vn.dencooper.fracejob.domain.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    User toUser(UserUpdationResquest request);

    UserResponse toUserResponse(User user);
}
