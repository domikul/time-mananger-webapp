package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.response.BucketRestModel;
import pl.wasko.time.manager.rest.api.model.response.EndRequestRestModel;
import pl.wasko.time.manager.rest.api.model.service.EndRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/end-request")
@AllArgsConstructor
public class EndRequestController {

    private final EndRequestService endRequestService;

    @PostMapping
    public ResponseEntity<EndRequestRestModel> sendEndRequest(@RequestBody EndRequestRestModel endRequestRestModel,
                                                              @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(endRequestService.sendEndRequest(endRequestRestModel, token));
    }

    @GetMapping
    public ResponseEntity<List<EndRequestRestModel>> getEndRequests(@RequestHeader(value = "Authorization") String token, @RequestParam Integer taskId) {
        final List<EndRequestRestModel> requestList = endRequestService.getRequests(token, taskId);
        return ResponseEntity.ok(requestList);
    }

}
