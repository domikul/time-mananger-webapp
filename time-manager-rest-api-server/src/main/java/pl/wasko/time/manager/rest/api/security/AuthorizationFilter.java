package pl.wasko.time.manager.rest.api.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.wasko.time.manager.rest.api.exception.NotAuthenticatedException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.WRONG_TOKEN;


public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = req.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }


        UsernamePasswordAuthenticationToken authentication = getAuthentication(token.substring(7));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null) {
            // parse the token.
            String email = JwtHelper.getSubjectFromToken(token);

            if (email != null) {
                return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}