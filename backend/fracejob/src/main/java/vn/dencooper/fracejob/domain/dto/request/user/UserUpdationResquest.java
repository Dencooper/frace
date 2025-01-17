package vn.dencooper.fracejob.domain.dto.request.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.Role;
import vn.dencooper.fracejob.utils.constant.GenderEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdationResquest {

    @NotNull
    @Size(min = 3, message = "Tên phải có tối thiểu 6 kí tự")
    String name;

    int age;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    String address;
    Company company;
    Role role;
}
