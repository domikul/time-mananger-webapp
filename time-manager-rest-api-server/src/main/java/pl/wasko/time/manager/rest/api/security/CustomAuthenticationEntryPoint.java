package pl.wasko.time.manager.rest.api.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.ExceptionView;
import pl.wasko.time.manager.rest.api.helper.ServletHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.UNAUTHORIZED;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ServletHelper.sendException(
                new ExceptionView(HttpServletResponse.SC_UNAUTHORIZED, authException.getClass().getName(), UNAUTHORIZED.getMessage()),
                response);
    }
}
