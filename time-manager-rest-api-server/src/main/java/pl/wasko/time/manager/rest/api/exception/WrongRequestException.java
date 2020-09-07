package pl.wasko.time.manager.rest.api.exception;

public class WrongRequestException extends RuntimeException {

    public WrongRequestException(String message) {
        super(message);
    }
}
