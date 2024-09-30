package vn.dencooper.fracejob.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.utils.JwtUtil;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "EMAIL_NOTBLANK")
    String email;

    @NotBlank(message = "NAME_NOTBLANK")
    String name;

    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "subscribers")
    @JoinTable(name = "skill_subscriber", joinColumns = @JoinColumn(name = "subcriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    List<Skill> skills;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = JwtUtil.getCurrentUserLogin().isPresent() ? JwtUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PostPersist
    public void handleBeforeUpdate() {
        this.updatedBy = JwtUtil.getCurrentUserLogin().isPresent() ? JwtUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
