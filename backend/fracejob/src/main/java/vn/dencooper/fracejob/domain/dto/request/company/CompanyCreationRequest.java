package vn.dencooper.fracejob.domain.dto.request.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyCreationRequest {
    @NotBlank(message = "Tên công ty không được để trống")
    String name;
    String description;
    String address;
    String logo;
}
