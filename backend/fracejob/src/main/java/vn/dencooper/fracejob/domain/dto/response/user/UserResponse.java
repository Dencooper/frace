package vn.dencooper.fracejob.domain.dto.response.user;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
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
    CompanyUserResponse company;
}
