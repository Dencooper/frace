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
import vn.dencooper.fracejob.domain.Job;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.job.JobResponse;
import vn.dencooper.fracejob.service.JobService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
    JobService jobService;

    @PostMapping
    @ApiMessage("Create a job")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody Job req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.handleCreateJob(req));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get a job")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(jobService.fetchJob(id));
    }

    @GetMapping
    @ApiMessage("Get all jobs")
    public ResponseEntity<PaginationResponse> getAllJobs(@Filter Specification<Job> spec,
            Pageable pageable) {

        PaginationResponse jobs = jobService.fetchAllJobs(spec, pageable);
        return ResponseEntity.ok().body(jobs);
    }

    @PutMapping
    @ApiMessage("Update a job")
    public ResponseEntity<JobResponse> updateJob(@Valid @RequestBody Job req) {
        return ResponseEntity.ok().body(jobService.handleUpdateJob(req));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) {
        jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }
}
