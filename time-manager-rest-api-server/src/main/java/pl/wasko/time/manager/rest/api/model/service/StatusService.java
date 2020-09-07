package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.model.repository.StatusRepository;
import pl.wasko.time.manager.rest.api.model.response.StatusRestModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<StatusRestModel> getAllStatuses() {
        return statusRepository.findAll().stream()
                .map(StatusRestModel::new)
                .collect(Collectors.toList());
    }
}
