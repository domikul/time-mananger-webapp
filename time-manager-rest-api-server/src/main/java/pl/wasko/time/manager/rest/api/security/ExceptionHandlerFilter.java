package pl.wasko.time.manager.rest.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.wasko.time.manager.rest.api.exception.ExceptionView;
import pl.wasko.time.manager.rest.api.exception.NotAuthenticatedException;
import pl.wasko.time.manager.rest.api.helper.ServletHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotAuthenticatedException e) {

            ExceptionView exceptionView = new ExceptionView(HttpStatus.UNAUTHORIZED.value(), e.getClass().getName(), e.getMessage());
            ServletHelper.sendException(exceptionView, response);
        }
    }


}
