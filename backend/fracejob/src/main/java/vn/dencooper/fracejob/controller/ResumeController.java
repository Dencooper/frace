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
import vn.dencooper.fracejob.domain.Resume;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeCreationResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeFetchReponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeUpdationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.service.JobService;
import vn.dencooper.fracejob.service.ResumeSevice;
import vn.dencooper.fracejob.service.UserService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResumeController {
    ResumeSevice resumeSevice;

    @PostMapping
    @ApiMessage("Create a resume")
    public ResponseEntity<ResumeCreationResponse> createResume(@Valid @RequestBody Resume req) {
        ResumeCreationResponse res = resumeSevice.handleCreateResume(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping
    @ApiMessage("Update a resume")
    public ResponseEntity<ResumeUpdationResponse> updateResume(@RequestBody Resume req) throws AppException {
        ResumeUpdationResponse res = resumeSevice.handleUpdateResume(req);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{id}")
    @ApiMessage("Get a resume by id")
    public ResponseEntity<ResumeFetchReponse> getResumeById(@PathVariable("id") long id) {
        Resume resume = resumeSevice.fetchById(id);
        ResumeFetchReponse res = resumeSevice.getResume(resume);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    @ApiMessage("Get all resumes")
    public ResponseEntity<PaginationResponse> getAllResumes(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        PaginationResponse res = resumeSevice.fetchAllResumes(spec, pageable);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) {
        resumeSevice.handleDeleteResume(id);
        return ResponseEntity.ok().body(null);
    }

}
