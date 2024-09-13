package vn.dencooper.fracejob.domain.dto;

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
