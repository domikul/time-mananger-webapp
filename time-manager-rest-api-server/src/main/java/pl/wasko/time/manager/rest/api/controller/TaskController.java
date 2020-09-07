package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.model.entity.Bucket;
import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.ShareRestModel;
import pl.wasko.time.manager.rest.api.model.response.TaskRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;
import pl.wasko.time.manager.rest.api.model.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<TaskRestModel> createTask(@RequestBody TaskRestModel taskRestModel,
                                                    @RequestHeader(value = "Authorization") String token)
            throws WrongRequestException {
        return ResponseEntity.ok(taskService.createTask(taskRestModel, token));
    }

    @PostMapping(value = "/share/{idTask}", consumes = APPLICATION_JSON_VALUE)

    public ResponseEntity<Set<ShareRestModel>> shareTask(@RequestBody final List<UserRestModel> userRestModels,
                                                         @PathVariable Integer idTask,
                                                         @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(taskService.shareTask(userRestModels, idTask, token));

    }

    @PutMapping(value = "/{idTask}")
    public ResponseEntity<TaskRestModel> updateTask(@RequestBody TaskRestModel taskRestModel,
                                                    @RequestHeader(value = "Authorization") String token,
                                                    @PathVariable Integer idTask)
            throws WrongRequestException {
        taskRestModel.setIdTask(idTask);
        return ResponseEntity.ok(taskService.updateTask(taskRestModel, token));
    }

    @GetMapping
    public ResponseEntity<List<TaskRestModel>> getBucketTasks(@RequestHeader(value = "Authorization") String token,
                                                              @RequestParam Integer bucketId) {
        return ResponseEntity.ok(taskService.getBucketTasks(bucketId, token));
    }


    @GetMapping(value = "/shared")
    public ResponseEntity<List<TaskRestModel>> getSharedTasks(@RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(taskService.getSharedTasks(token));
    }

    @GetMapping(value = "/share/{idTask}")
    public ResponseEntity<List<ShareRestModel>> getTaskShares(@PathVariable Integer idTask,
                                                            @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(taskService.getTaskShares(idTask, token));
    }

    @GetMapping(value = "/history/{idTask}")
    public ResponseEntity<List<HistoryRestModel>> getTaskHistory(@RequestHeader(value = "Authorization") String token, @PathVariable Integer idTask) {
        final List<HistoryRestModel> historyList = taskService.getHistory(token, idTask);
        return ResponseEntity.ok(historyList);
    }


    @DeleteMapping(value = "/{idTask}")
    public ResponseEntity<Void> deleteTask(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable Integer idTask) {

        taskService.deleteTask(idTask, token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/share/{idTask}")
    public ResponseEntity<Void> deleteTaskShare(@RequestHeader(value = "Authorization") String token,
                                                @PathVariable Integer idTask,
                                                @RequestParam Integer userId) {

        taskService.deleteTaskShare(idTask, userId, token);
        return ResponseEntity.noContent().build();
    }

}
