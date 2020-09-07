package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.repository.EndRequestRepository;
import pl.wasko.time.manager.rest.api.model.repository.TaskRepository;
import pl.wasko.time.manager.rest.api.model.response.EndRequestRestModel;
import pl.wasko.time.manager.rest.api.scheduling.MailService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class EndRequestService {

    private final TaskRepository taskRepository;
    private final EndRequestRepository endRequestRepository;
    private final JwtHelper jwtHelper;
    private final MailService mailService;
    private final TaskDeleter taskDeleter;

    public EndRequestRestModel sendEndRequest(EndRequestRestModel endRequestRestModel, String token) {

        Task task = taskRepository.findById(endRequestRestModel.getTaskId()).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (!task.isUserCoOwner(currentUser) || task.isUserOwner(currentUser))
            throw new NotAuthorizedActionException(CANNOT_SEND_END_REQUEST.getMessage());

        if(endRequestRepository.existsByUserAndTask(currentUser, task))
            throw new WrongRequestException(ALREADY_END_REQUESTED.getMessage());

        EndRequest endRequest = new EndRequest();
        endRequest.setTask(task);
        endRequest.setUser(currentUser);
        taskDeleter.deleteUserTimer(task, currentUser);

        new Thread(() -> mailService.sendEndRequestNotification(task, currentUser)).start();

        return new EndRequestRestModel(endRequestRepository.save(endRequest));
    }

    public List<EndRequestRestModel> getRequests(String token, Integer taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage())
        );

        if(!task.isUserOwner(jwtHelper.getUserFromToken(token))){
            throw new NotAuthorizedActionException(NOT_AUTHORIZED.getMessage());
        }

        return task.getListOfEndRequests().stream()
                .map(EndRequestRestModel::new).collect(Collectors.toList());
    }

}
