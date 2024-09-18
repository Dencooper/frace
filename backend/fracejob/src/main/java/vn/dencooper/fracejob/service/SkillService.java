package vn.dencooper.fracejob.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Skill;
import vn.dencooper.fracejob.domain.dto.response.Meta;
import vn.dencooper.fracejob.domain.dto.response.PaginationResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.repository.SkillRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkillService {
    SkillRepository skillRepository;

    public Skill handleCreateSkill(Skill req) throws AppException {
        if (req.getName() != null && skillRepository.existsByName(req.getName())) {
            throw new AppException(ErrorCode.SKILL_EXISTED);
        } else {
            return skillRepository.save(req);
        }
    }

    public Skill handleUpdateSkill(Skill req) throws AppException {
        Skill skill = skillRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOTFOUND));
        if (skill.getName() != null && skillRepository.existsByName(req.getName())) {
            throw new AppException(ErrorCode.SKILL_EXISTED);
        }
        skill.setName(req.getName());
        return skillRepository.save(skill);
    }

    public PaginationResponse fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkills = skillRepository.findAll(spec, pageable);

        if (pageSkills.getTotalElements() == 0) {
            throw new AppException(ErrorCode.SKILL_NOTFOUND);
        }
        PaginationResponse res = new PaginationResponse();
        Meta meta = Meta.builder()
                .current(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageSkills.getTotalPages())
                .total(pageSkills.getTotalElements())
                .build();
        res.setMeta(meta);
        res.setResult(pageSkills.getContent());

        return res;
    }

    public void handleDeleteSkill(long id) throws AppException {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SKILL_NOTFOUND));
        skill.getJobs().forEach((job) -> job.getSkills().remove(skill));

        this.skillRepository.delete(skill);
    }

}
