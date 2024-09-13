package vn.dencooper.fracejob.domain.dto.response;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.utils.constant.GenderEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String email;
    String name;
    GenderEnum gender;
    String address;
    int age;
    Instant updatedAt;
    Instant createdAt;
    String updatedBy;
    String createdBy;
}
