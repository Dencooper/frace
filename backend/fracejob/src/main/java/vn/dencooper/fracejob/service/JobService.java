package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Company;
import vn.dencooper.fracejob.domain.Job;
import vn.dencooper.fracejob.domain.Resume;
import vn.dencooper.fracejob.domain.User;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.job.JobResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.JobMapper;
import vn.dencooper.fracejob.repository.JobRepository;
import vn.dencooper.fracejob.repository.SkillRepository;
import vn.dencooper.fracejob.utils.JwtUtil;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {
    JobRepository jobRepository;
    JobMapper jobMapper;

    UserService userService;

    SkillRepository skillRepository;

    public JobResponse handleCreateJob(Job req) {
        if (req.getSkills() != null) {
            List<Long> listIdSkill = req.getSkills()
                    .stream()
                    .map(skill -> skill.getId())
                    .toList();
            req.setSkills(skillRepository.findByIdIn(listIdSkill));
        }

        Job job = jobRepository.save(req);

        JobResponse res = jobMapper.toJobResponse(job);

        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream()
                    .map(skill -> skill.getName())
                    .toList();

            res.setSkills(skills);
        }

        return res;
    }

    public JobResponse handleUpdateJob(Job req) throws AppException {
        Job currentJob = jobRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOTFOUND));

        if (req.getSkills() != null) {
            List<Long> listIdSkill = req.getSkills()
                    .stream()
                    .map(skill -> skill.getId())
                    .toList();
            req.setSkills(skillRepository.findByIdIn(listIdSkill));
        }

        jobMapper.toJob(currentJob, req);

        Job job = jobRepository.save(currentJob);

        JobResponse res = jobMapper.toJobResponse(job);

        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream()
                    .map(skill -> skill.getName())
                    .toList();

            res.setSkills(skills);
        }

        return res;
    }

    public Job fetchJob(@PathVariable("id") long id) throws AppException {
        return jobRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.JOB_NOTFOUND));
    }

    public PaginationResponse fetchAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJobs = jobRepository.findAll(spec, pageable);

        PaginationResponse paginationResponse = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageJobs.getTotalPages())
                .total(pageJobs.getTotalElements())
                .build();
        paginationResponse.setMeta(meta);
        paginationResponse.setResult(pageJobs.getContent());

        return paginationResponse;
    }

    public void handleDeleteJob(long id) throws AppException {
        if (!jobRepository.existsById(id)) {
            throw new AppException(ErrorCode.JOB_NOTFOUND);
        }
        jobRepository.deleteById(id);
    }

    public PaginationResponse fetchAllJobsByCompany(Pageable pageable) {
        List<Job> companyJobs = null;
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = userService.fetchUserByEmail(email);
        Company userCompany = currentUser.getCompany();
        if (userCompany != null) {
            companyJobs = userCompany.getJobs();
        }
        int totalPages = companyJobs.size() % pageable.getPageSize() == 0
                ? companyJobs.size() / pageable.getPageSize()
                : (companyJobs.size() / pageable.getPageSize()) + 1;
        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(totalPages)
                .total(companyJobs.size())
                .build();
        res.setMeta(meta);
        res.setResult(companyJobs);

        return res;
    }

}
