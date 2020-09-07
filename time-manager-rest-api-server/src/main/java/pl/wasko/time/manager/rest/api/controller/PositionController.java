package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.response.PositionRestModel;
import pl.wasko.time.manager.rest.api.model.response.RoleRestModel;
import pl.wasko.time.manager.rest.api.model.service.PositionService;

import java.util.List;

@RestController

@AllArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping(value = "/position")
    public ResponseEntity<List<PositionRestModel>> getPositions() {
        final List<PositionRestModel> positionList = positionService.getAllPositions();
        return ResponseEntity.ok(positionList);
    }

}
