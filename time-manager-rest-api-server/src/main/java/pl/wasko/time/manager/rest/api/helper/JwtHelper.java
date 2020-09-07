package pl.wasko.time.manager.rest.api.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthenticatedException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.auth.Credentials;
import pl.wasko.time.manager.rest.api.model.auth.Token;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.UserRepository;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class JwtHelper {

    private static final int VALIDATION_TIME = 168 * 3600; //7 days
    private static final String SECRET = "ppl_itr_2020";
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    private User getUserFromSubject(String emailName) {
        Email email = emailRepository.findByEmailName(emailName).orElseThrow(() ->
                new WrongRequestException(TOKEN_NOT_VALID_EMAIL.getMessage())
        );
        return userRepository.findByEmail(email).orElseThrow(() ->
                new WrongRequestException(TOKEN_NOT_ANY_USER.getMessage())
        );
    }

    public User getUserFromToken(String token) throws WrongRequestException {
        if(token.startsWith("Bearer "))
            token = token.substring(7);

        String emailName = getSubjectFromToken(token);
        User user = getUserFromSubject(emailName);
        if(user.getActive().equals(false))
            throw new NotAuthenticatedException(NOT_ACTIVATED.getMessage());

        return user;
    }

    private Date calculateExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(Instant.now()));
        c.add(Calendar.MILLISECOND, VALIDATION_TIME * 1000);
        return c.getTime();
    }

    public Token getToken(Credentials credentials) {
        Token token = new Token();
        Date expirationDate = calculateExpirationDate();
        User user = getUserFromSubject(credentials.getEmail());
        token.setToken(
                JWT.create()
                        .withIssuedAt(new Date())
                        .withSubject(credentials.getEmail())
                        .withExpiresAt(expirationDate)
                        .withClaim("active", user.getActive())
                        .sign(HMAC512(SECRET.getBytes()))
        );

        token.setUser(new UserRestModel(user));
        token.setExpirationDate(expirationDate);
        return token;
    }

    public static String getSubjectFromToken(String token) {
        DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token);

        if(decoded.getClaim("active").asBoolean().equals(false))
            throw new NotAuthenticatedException(NOT_ACTIVATED.getMessage());

        return decoded.getSubject();
    }
}
