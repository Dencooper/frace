package vn.dencooper.fracejob.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.service.SubscriberService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    SubscriberService subscriberService;

    @GetMapping
    @ApiMessage("Send email")
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public String sendEmail() {
        subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}
