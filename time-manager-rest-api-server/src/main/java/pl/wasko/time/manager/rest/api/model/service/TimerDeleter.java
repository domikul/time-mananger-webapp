package pl.wasko.time.manager.rest.api.model.service;

import pl.wasko.time.manager.rest.api.model.entity.Timer;

import java.util.List;

public interface TimerDeleter {

    void deleteTimers(List<Timer> timersToDelete);

    void suspendTimers(List<Timer> timersToSuspend);
}
