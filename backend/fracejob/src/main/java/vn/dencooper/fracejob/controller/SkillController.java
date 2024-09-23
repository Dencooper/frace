package vn.dencooper.fracejob.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Skill;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.service.SkillService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkillController {
    SkillService skillService;

    @PostMapping
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.handleCreateSkill(req));
    }

    @PutMapping
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> udpateSkill(@Valid @RequestBody Skill req) throws AppException {
        return ResponseEntity.ok().body(skillService.handleUpdateSkill(req));
    }

    @GetMapping
    @ApiMessage("Fetch all skills")
    public ResponseEntity<PaginationResponse> getAllSkills(@Filter Specification<Skill> spec,
            Pageable pageable) {
        PaginationResponse skills = skillService.fetchAllSkill(spec, pageable);
        return ResponseEntity.ok().body(skills);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) {
        skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
