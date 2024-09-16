package vn.dencooper.fracejob.domain.dto.response.job;

import java.time.Instant;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.utils.constant.LevelEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
    long id;
    String name;
    String location;
    double salary;
    int quantity;
    LevelEnum level;
    String description;

    Instant startDate;
    Instant endDate;
    boolean active;

    List<String> skills;

    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
}
