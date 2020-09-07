package pl.wasko.time.manager.rest.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionView {
    Integer status;
    String name;
    String message;
}
