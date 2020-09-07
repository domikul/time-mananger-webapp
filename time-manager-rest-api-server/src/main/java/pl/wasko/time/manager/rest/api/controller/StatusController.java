package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.response.StatusRestModel;
import pl.wasko.time.manager.rest.api.model.service.StatusService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping(path = "/status")
    public ResponseEntity<List<StatusRestModel>> getStatuses() {
        final List<StatusRestModel> statusesList = statusService.getAllStatuses();
        return ResponseEntity.ok(statusesList);
    }

}
