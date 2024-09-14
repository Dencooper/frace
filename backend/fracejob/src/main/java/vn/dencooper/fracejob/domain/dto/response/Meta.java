package vn.dencooper.fracejob.domain.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Meta {
    int current;
    int pageSize;
    int pages;
    long total;
}
