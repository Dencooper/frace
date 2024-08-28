package vn.dencooper.fracejob.domain.dto.response;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.utils.constant.GenderEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String email;
    String fullName;
    int age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
    String refreshToken;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
}
