package vn.dencooper.fracejob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Resume;
import vn.dencooper.fracejob.domain.Skill;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.domain.dto.response.job.JobResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeCreationResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeFetchReponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeUpdationResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeFetchReponse.UserResume;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.mapper.ResumeMapper;
import vn.dencooper.fracejob.repository.JobRepository;
import vn.dencooper.fracejob.repository.ResumeRepository;
import vn.dencooper.fracejob.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResumeSevice {
    ResumeRepository resumeRepository;
    UserRepository userRepository;
    JobRepository jobRepository;
    ResumeMapper resumeMapper;

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null) {
            return false;
        }
        if (!userRepository.findById(resume.getUser().getId()).isPresent()) {
            return false;
        }
        if (resume.getJob() == null) {
            return false;
        }
        if (!jobRepository.findById(resume.getJob().getId()).isPresent()) {
            return false;
        }
        return true;
    }

    public ResumeCreationResponse handleCreateResume(Resume req) throws AppException {
        boolean isExistUserAndJob = checkResumeExistByUserAndJob(req);
        if (!isExistUserAndJob) {
            throw new AppException(ErrorCode.USERJOB_NOTEXISTED);
        }
        Resume res = resumeRepository.save(req);
        return resumeMapper.toResumeCreationResponse(res);
    }

    public ResumeUpdationResponse handleUpdateResume(Resume req) {
        Resume resume = resumeRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RESUME_NOTEXISTED));
        resume.setStatus(req.getStatus());
        Resume res = resumeRepository.save(resume);
        return resumeMapper.toResumeUpdationResponse(res);
    }

    public ResumeFetchReponse fetchResumeById(long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESUME_NOTEXISTED));
        ResumeFetchReponse res = resumeMapper.toResumeFetchReponse(resume);
        res.setUser(new ResumeFetchReponse.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResumeFetchReponse.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return res;
    }

    public PaginationResponse fetchAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResumes = resumeRepository.findAll(spec, pageable);

        if (pageResumes.getTotalElements() == 0) {
            throw new AppException(ErrorCode.RESUME_NOTFOUND);
        }
        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .current(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageResumes.getTotalPages())
                .total(pageResumes.getTotalElements())
                .build();
        res.setMeta(meta);
        res.setResult(pageResumes
                .getContent()
                .stream()
                .map((resume) -> {
                    ResumeFetchReponse resumeFetchReponse = resumeMapper.toResumeFetchReponse(resume);
                    resumeFetchReponse.setUser(
                            new ResumeFetchReponse.UserResume(resume.getUser().getId(), resume.getUser().getName()));
                    resumeFetchReponse.setJob(
                            new ResumeFetchReponse.JobResume(resume.getJob().getId(), resume.getJob().getName()));
                    return resumeFetchReponse;
                })
                .toList());

        return res;
    }

    public void handleDeleteResume(long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESUME_NOTEXISTED));
        resumeRepository.delete(resume);
    }

}
