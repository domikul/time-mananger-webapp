package pl.wasko.time.manager.rest.api.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.response.HistoryElementRestModel;
import pl.wasko.time.manager.rest.api.model.response.PositionRestModel;
import pl.wasko.time.manager.rest.api.model.service.HistoryElementService;

import java.util.List;

@RestController
@RequestMapping(path = "/history")
@AllArgsConstructor
public class HistoryElementController {

    private final HistoryElementService historyElementService;

    @GetMapping
    public ResponseEntity<List<HistoryElementRestModel>> getHistoryElements() {
        final List<HistoryElementRestModel> historyElementsList = historyElementService.getAllHistoryElements();
        return ResponseEntity.ok(historyElementsList);
    }

}
