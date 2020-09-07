package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.model.repository.PositionRepository;
import pl.wasko.time.manager.rest.api.model.response.PositionRestModel;
import pl.wasko.time.manager.rest.api.model.response.RoleRestModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<PositionRestModel> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(PositionRestModel::new)
                .collect(Collectors.toList());
    }

}
