package pl.wasko.time.manager.rest.api.exception;

public class NotAuthorizedActionException extends RuntimeException {

    public NotAuthorizedActionException(String message) {
        super(message);
    }
}
