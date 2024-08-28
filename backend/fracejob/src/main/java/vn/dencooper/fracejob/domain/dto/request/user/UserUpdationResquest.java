package vn.dencooper.fracejob.domain.dto.request.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UserUpdationResquest {

    @NotNull
    @Size(min = 3, message = "Tên phải có tối thiểu 6 kí tự")
    String fullName;

    int age;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    String address;
}