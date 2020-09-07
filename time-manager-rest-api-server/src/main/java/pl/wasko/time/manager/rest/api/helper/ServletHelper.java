package pl.wasko.time.manager.rest.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import pl.wasko.time.manager.rest.api.exception.ExceptionView;

import javax.servlet.http.HttpServletResponse;

public class ServletHelper {

    private static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SneakyThrows
    public static void sendException(ExceptionView exceptionView, HttpServletResponse response) {
        response.setStatus(exceptionView.getStatus());
        response.getWriter().write(convertObjectToJson(exceptionView));
    }
}
