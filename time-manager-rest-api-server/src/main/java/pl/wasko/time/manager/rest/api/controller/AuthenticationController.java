package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.exception.NotAuthenticatedException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.auth.Credentials;
import pl.wasko.time.manager.rest.api.model.auth.Token;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.WRONG_CREDENTIALS;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtHelper jwtHelper;

    @PostMapping(path="/login")
    public Token login(@RequestBody Credentials credentials) throws NotAuthenticatedException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
            return jwtHelper.getToken(credentials);

        } catch (AuthenticationException e) {
            System.out.println(WRONG_CREDENTIALS.getMessage());
            throw new NotAuthenticatedException(WRONG_CREDENTIALS.getMessage());
        }
    }
}
