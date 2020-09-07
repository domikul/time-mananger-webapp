package pl.wasko.time.manager.rest.api.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.repository.PriorityRepository;
import pl.wasko.time.manager.rest.api.model.response.PriorityRestModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/priority")
@AllArgsConstructor
public class PriorityController {

    private final PriorityRepository priorityRepository;

    @GetMapping
    ResponseEntity<List<PriorityRestModel>> getPriorities() {
        return ResponseEntity.ok(priorityRepository.findAll().stream().map(PriorityRestModel::new).collect(Collectors.toList()));
    }
}
