package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.model.repository.HistoryElementRepository;
import pl.wasko.time.manager.rest.api.model.response.HistoryElementRestModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryElementService {

    private final HistoryElementRepository historyElementRepository;

    public HistoryElementService(HistoryElementRepository historyElementRepository) {
        this.historyElementRepository = historyElementRepository;
    }

    public List<HistoryElementRestModel> getAllHistoryElements() {
        return historyElementRepository.findAll().stream()
                .map(HistoryElementRestModel::new)
                .collect(Collectors.toList());
    }

}
