package pl.wasko.time.manager.rest.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException(String message) {
        super(message);
    }

}
