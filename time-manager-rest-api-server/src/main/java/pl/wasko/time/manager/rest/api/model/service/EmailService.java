package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.UserRepository;
import pl.wasko.time.manager.rest.api.model.response.EmailRestModel;
import pl.wasko.time.manager.rest.api.scheduling.MailScheduleDataHolder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final MailScheduleDataHolder mailScheduleDataHolder;

    public EmailService(EmailRepository emailRepository, UserRepository userRepository, MailScheduleDataHolder mailScheduleDataHolder) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.mailScheduleDataHolder = mailScheduleDataHolder;
    }

    public List<EmailRestModel> getAll() {
        return emailRepository.findAll().stream()
                .map(EmailRestModel::new)
                .collect(Collectors.toList());
    }

    public List<EmailRestModel> getEmailsWithoutUsers() {
        List<Email> emails = new ArrayList<>(emailRepository.findAll());
        List<Email> freeMails = new ArrayList<>();

        for(Email e : emails){
            if(!userRepository.existsUserByEmail(e.getIdEmail()).equals(BigInteger.valueOf(1))){
                freeMails.add(e);
            }
        }

        return freeMails.stream().map(EmailRestModel::new).collect(Collectors.toList());
    }

    public EmailRestModel add(final EmailRestModel email) {

        Optional<Email> emailOptional = emailRepository.findByEmailName(email.getEmailName());
        if (emailOptional.isPresent()) {
            throw new WrongRequestException(EMAIL_ALREADY_EXISTS.getMessage());
        } else {
            Email newEmail = mapRestModel(email);
            newEmail = emailRepository.save(newEmail);
            mailScheduleDataHolder.onEmailAdded(newEmail);
            return new EmailRestModel(newEmail);
        }

    }

    private Email mapRestModel(final EmailRestModel model) {
        return new Email(model.getIdEmail(), model.getEmailName());
    }

    public void deletePermanet(final Integer id) {

        Optional<Email> email = emailRepository.findById(id);

        if (!emailRepository.existsByIdEmail(id))
            throw new WrongRequestException(EMAIL_NOT_FOUND.getMessage());
        else if (userRepository.existsUserByEmail(id).equals(BigInteger.valueOf(1)))
            throw new WrongRequestException(EMAIL_IN_USE.getMessage());
        else if (!email.get().getListOfSubscriptions().isEmpty())
            throw new WrongRequestException(EMAIL_IN_USE.getMessage());
        else {
            mailScheduleDataHolder.onEmailDeleting(email.get());
            emailRepository.deleteById(id);
        }


    }

}
