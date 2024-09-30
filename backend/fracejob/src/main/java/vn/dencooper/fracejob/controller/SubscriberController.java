package vn.dencooper.fracejob.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.dencooper.fracejob.domain.Subscriber;
import vn.dencooper.fracejob.service.SubscriberService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/subscribers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberController {
    SubscriberService subscriberService;

    @PostMapping
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.handleCreateSubscriber(req));
    }

    @PutMapping
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@Valid @RequestBody Subscriber req) {
        return ResponseEntity.ok().body(subscriberService.handleUpdateSubscriber(req));
    }
}
