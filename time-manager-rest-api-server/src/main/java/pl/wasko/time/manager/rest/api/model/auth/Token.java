package pl.wasko.time.manager.rest.api.model.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;

import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Token {

    private String token;

    private Date expirationDate;

    private UserRestModel user;
}
