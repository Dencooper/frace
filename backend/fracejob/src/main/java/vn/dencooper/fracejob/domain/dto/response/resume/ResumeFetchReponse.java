package vn.dencooper.fracejob.domain.dto.response.resume;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.utils.constant.ResumeStateEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResumeFetchReponse {
    long id;
    String email;
    String url;

    @Enumerated(EnumType.STRING)
    ResumeStateEnum status;

    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    UserResume user;
    JobResume job;

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserResume {
        long id;
        String name;
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JobResume {
        long id;
        String name;
    }
}
