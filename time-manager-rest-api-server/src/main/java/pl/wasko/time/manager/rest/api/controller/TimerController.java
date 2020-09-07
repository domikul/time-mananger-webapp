package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.model.response.TimerRestModel;
import pl.wasko.time.manager.rest.api.model.service.TimerService;

@RestController
@RequestMapping(path = "/timer")
@AllArgsConstructor
public class TimerController {


    private final TimerService timerService;

    @PostMapping
    public ResponseEntity<TimerRestModel> startNewTimer(@RequestBody TimerRestModel timerRestModel,
                                                     @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(timerService.startNewTimer(timerRestModel, token));
    }

    @DeleteMapping(value = "/{idTimer}")
    public ResponseEntity<Void> stopTimer(@PathVariable Integer idTimer,
                                          @RequestHeader(value = "Authorization") String token) {
        timerService.stopTimer(idTimer, token);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/suspend/{idTimer}")
    public ResponseEntity<TimerRestModel> suspendTimer(@PathVariable Integer idTimer,
                                                       @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(timerService.suspendTimer(idTimer, token));
    }

    @PatchMapping(value = "/resume/{idTimer}")
    public ResponseEntity<TimerRestModel> resumeTimer(@PathVariable Integer idTimer,
                                                       @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(timerService.resumeTimer(idTimer, token));
    }
}
