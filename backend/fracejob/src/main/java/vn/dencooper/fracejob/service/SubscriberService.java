package vn.dencooper.fracejob.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Skill;
import vn.dencooper.fracejob.domain.Subscriber;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.repository.SkillRepository;
import vn.dencooper.fracejob.repository.SubscriberRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberService {
    SubscriberRepository subscriberRepository;
    SkillRepository skillRepository;

    public Subscriber handleCreateSubscriber(Subscriber req) throws AppException {
        if (subscriberRepository.existByEmail(req.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (req.getSkills() != null) {
            List<Long> listIdSkills = req.getSkills()
                    .stream()
                    .map(skill -> skill.getId())
                    .toList();
            List<Skill> listSkills = skillRepository.findByIdIn(listIdSkills);
            req.setSkills(listSkills);
        }
        return subscriberRepository.save(req);
    }

    public Subscriber handleUpdateSubscriber(Subscriber req) throws AppException {
        Subscriber res = subscriberRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIBER_NOTFOUND));
        if (req.getSkills() != null) {
            List<Long> listIdSkills = req.getSkills()
                    .stream()
                    .map(skill -> skill.getId())
                    .toList();
            List<Skill> listSkills = skillRepository.findByIdIn(listIdSkills);
            res.setSkills(listSkills);
        }
        return subscriberRepository.save(res);
    }
}
