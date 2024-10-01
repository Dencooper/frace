package vn.dencooper.fracejob.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Job;
import vn.dencooper.fracejob.domain.Skill;
import vn.dencooper.fracejob.domain.Subscriber;
import vn.dencooper.fracejob.domain.dto.response.email.EmailRespone;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.repository.JobRepository;
import vn.dencooper.fracejob.repository.SkillRepository;
import vn.dencooper.fracejob.repository.SubscriberRepository;
import vn.dencooper.fracejob.utils.JwtUtil;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberService {
    SubscriberRepository subscriberRepository;
    SkillRepository skillRepository;
    JobRepository jobRepository;
    EmailService emailService;

    public Subscriber handleCreateSubscriber(Subscriber req) throws AppException {
        if (subscriberRepository.existsByEmail(req.getEmail())) {
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

    public EmailRespone convertJobToSendEmail(Job job) {
        EmailRespone res = new EmailRespone();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new EmailRespone.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<EmailRespone.SkillEmail> s = skills.stream().map(skill -> new EmailRespone.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                        List<EmailRespone> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    public Subscriber findByEmail() {
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        return subscriberRepository.findByEmail(email);
    }
}
