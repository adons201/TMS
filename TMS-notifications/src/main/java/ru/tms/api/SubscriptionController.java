package ru.tms.api;

import org.springframework.web.bind.annotation.*;
import ru.tms.dto.Subscription;
import ru.tms.service.SubscriptionService;
import ru.tms.service.SubscriptionServiceImpl;

@RestController
@RequestMapping("/tms_notification")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionServiceImpl subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscription")
    public Boolean getSubscribeFlag(@RequestParam("targetType") String targetType,
                                    @RequestParam("targetObjectId") Long targetObjectId,
                                    @RequestParam("userId") String userId) {
        return subscriptionService.getSubscribeFlag(targetType, targetObjectId, userId);
    }

    @PostMapping("/subscription")
    public Subscription createSubscription(@RequestParam("targetType") String targetType,
                                           @RequestParam("targetObjectId") Long targetObjectId,
                                           @RequestParam("userId") String userId) {
        return subscriptionService.createSubscription(targetType, targetObjectId, userId);
    }

    @DeleteMapping("/subscription")
    public void deleteSubscription(@RequestParam("targetType") String targetType,
                                   @RequestParam("targetObjectId") Long targetObjectId,
                                   @RequestParam("userId") String userId) {
        subscriptionService.deleteSubscription(targetType, targetObjectId, userId);
    }
}