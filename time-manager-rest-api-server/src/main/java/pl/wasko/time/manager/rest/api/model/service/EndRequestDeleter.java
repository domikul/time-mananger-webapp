package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.EndRequest;
import pl.wasko.time.manager.rest.api.model.entity.EndRequestKey;
import pl.wasko.time.manager.rest.api.model.entity.Share;
import pl.wasko.time.manager.rest.api.model.repository.EndRequestRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class EndRequestDeleter {

    private final EndRequestRepository endRequestRepository;

    public void deleteEndRequests(List<EndRequest> endRequestsToDelete) {
        endRequestRepository.deleteAll(endRequestsToDelete);
    }

    public void deleteSingleShareEndRequests(Share share) {
        try{
            EndRequest endRequest = endRequestRepository.getOne(new EndRequestKey(
                    share.getUser().getIdUser(), share.getTask().getIdTask()));

            deleteEndRequests(Collections.singletonList(endRequest));
        } catch (EntityNotFoundException ignored) {}
    }

    public void deleteSingleCoOwnerEndRequests(CoOwner coOwner) {
        List<EndRequest> endRequests = endRequestRepository.findAllByTaskIsInAndUser(
                coOwner.getBucket().getListOfTasks(), coOwner.getUser());

        deleteEndRequests(endRequests);
    }

}
