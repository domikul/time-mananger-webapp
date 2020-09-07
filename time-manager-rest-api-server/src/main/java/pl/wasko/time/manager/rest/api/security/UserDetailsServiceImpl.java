package pl.wasko.time.manager.rest.api.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthenticatedException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.UserRepository;

import static java.util.Collections.emptyList;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Email email = emailRepository.findByEmailName(username).orElseThrow(() -> new WrongRequestException("Wrong email"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new WrongRequestException("Wrong email"));
        return new org.springframework.security.core.userdetails.User(email.getEmailName(), user.getPassword(), emptyList());
    }
}