package vn.dencooper.fracejob.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationResponse {
    Meta meta;
    Object result;
}
