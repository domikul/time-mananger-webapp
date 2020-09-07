package pl.wasko.time.manager.rest.api.model.service;

import pl.wasko.time.manager.rest.api.model.entity.User;

public interface ShareDeleter {

    void deleteUserShares(User user);
}
