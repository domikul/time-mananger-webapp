package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.model.response.SubscriptionRestModel;
import pl.wasko.time.manager.rest.api.model.response.DetailedSubscriptionRestModel;
import pl.wasko.time.manager.rest.api.model.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping(path = "/subscription")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionRestModel> addSubscription(@RequestBody SubscriptionRestModel subscriptionRestModel,
                                                                 @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(subscriptionService.addSubscription(subscriptionRestModel, token));
    }

    @DeleteMapping(value = "/{idSub}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer idSub,
                                                   @RequestHeader(value = "Authorization") String token) {

        subscriptionService.deleteSubscription(idSub, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionRestModel>> getTaskSubscriptions(@RequestParam Integer taskId,
                                                                            @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(subscriptionService.getTaskSubscriptions(taskId, token));
    }

    @GetMapping(value = "/own")
    public ResponseEntity<List<DetailedSubscriptionRestModel>> getOwnSubscriptions(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(subscriptionService.getOwnSubscriptions(token));
    }

    @GetMapping(value = "/shared")
    public ResponseEntity<List<DetailedSubscriptionRestModel>> getSharedSubscriptions(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(subscriptionService.getSharedSubscriptions(token));
    }
}

