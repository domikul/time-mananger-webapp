package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.RoleEnum;
import pl.wasko.time.manager.rest.api.model.repository.*;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;
import pl.wasko.time.manager.rest.api.scheduling.MailScheduleDataHolder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailRepository emailRepository;
    private final PositionRepository positionRepository;
    private final BucketRepository bucketRepository;
    private final TaskRepository taskRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final HistoryService historyService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtHelper jwtHelper;
    private final SubscriptionManager subscriptionManager;
    private final TaskDeleter taskDeleter;
    private final ShareDeleter shareDeleter;
    private final CoOwnerDeleter coOwnerDeleter;
    private final MailScheduleDataHolder mailScheduleDataHolder;

    private void onUserDeactivated(User user) {
        for(Task task : user.getListOfTasks())
            taskDeleter.deleteUserTimer(task, user);
    }

    private void onUserDeleting(User deleter, User user) {
        shareDeleter.deleteUserShares(user);
        coOwnerDeleter.deleteUserCoOwners(user);
        List<Bucket> userBuckets = user.getListOfBuckets().stream()
                .peek(bucket -> bucket.setUser(deleter))
                .collect(Collectors.toList());

        bucketRepository.saveAll(userBuckets);

        List<Task> userTasks = taskRepository.findAllByUser(user).stream()
                .peek(task -> task.setUser(task.getBucket().getUser()))
                .collect(Collectors.toList());

        taskRepository.saveAll(userTasks);


    }

    public UserRestModel registerUser(UserRestModel userRestModel, String token) {

        Optional<Email> emailOptional = emailRepository.findByEmailName(userRestModel.getEmail());

        Email email;
        if (!emailOptional.isPresent()) {
            email = new Email();
            email.setEmailName(userRestModel.getEmail());
            emailRepository.save(email);
            mailScheduleDataHolder.onEmailAdded(email);
        } else if (userRepository.existsUserByEmail(emailOptional.get().getIdEmail()).equals(BigInteger.valueOf(1))) {
            throw new WrongRequestException(WRONG_EMAIL.getMessage());
        } else
            email = emailOptional.get();

        User user = new User();

        user.setFirstName(userRestModel.getFirstName());
        user.setLastName(userRestModel.getLastName());
        user.setEmail(email);
        user.setPosition(positionRepository.findById(userRestModel.getPositionId()).orElseThrow(() ->
                new ResourceNotFoundException(POSITION_NOT_FOUND.getMessage())
        ));
        user.setRole(roleRepository.findById(userRestModel.getRoleId()).orElseThrow(() ->
                new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage())
        ));
        user.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));
        user.setDeleted(false);
        user.setActive(true);
        userRepository.save(user);
        User currentUser = jwtHelper.getUserFromToken(token);
        historyService.saveToHistory(user, currentUser, OperationTypeEnum.CREATE);

        return new UserRestModel(user);
    }

    public List<UserRestModel> getAll() {
        return userRepository.findAll().stream()
                .map(UserRestModel::new)
                .collect(Collectors.toList());
    }

    public UserRestModel update(UserRestModel userRestModel, Integer idUser, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);
        User editedUser = userRepository.findById(idUser).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));

        Email email = editedUser.getEmail();


        if (currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN)) {

            if(!editedUser.getEmail().getEmailName().equals(userRestModel.getEmail())) {
                Optional<Email> emailOptional = emailRepository.findByEmailName(userRestModel.getEmail());
                if (!emailOptional.isPresent()) {

                    email.setEmailName(userRestModel.getEmail());
                    emailRepository.save(email);
                    mailScheduleDataHolder.onEmailChanged(email);
                } else if (!userRepository.existsUserByEmail(emailOptional.get().getIdEmail()).equals(BigInteger.valueOf(1))) {
                    editedUser.setEmail(emailOptional.get());
                    userRepository.save(editedUser);
                    subscriptionManager.onEmailUpdated(email, emailOptional.get());
                    if(!subscriptionRepository.existsByEmail(email))
                        emailRepository.delete(email);
                } else
                    throw new WrongRequestException(WRONG_EMAIL.getMessage());

            }
            editedUser.setFirstName(userRestModel.getFirstName());
            editedUser.setLastName(userRestModel.getLastName());
            editedUser.setPosition(positionRepository.findById(userRestModel.getPositionId()).orElseThrow(() ->
                    new ResourceNotFoundException(POSITION_NOT_FOUND.getMessage())));
            if(userRestModel.getPassword() != null && !userRestModel.getPassword().equals(""))
                editedUser.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));

            if(editedUser.getActive() && !userRestModel.getActive())
                onUserDeactivated(editedUser);

            editedUser.setActive(userRestModel.getActive());
            historyService.saveToHistory(editedUser, currentUser, OperationTypeEnum.UPDATE);
            return new UserRestModel(userRepository.save(editedUser));

        } else if (currentUser.getIdUser().equals(idUser)){

            editedUser.setFirstName(userRestModel.getFirstName());
            editedUser.setLastName(userRestModel.getLastName());
            if(userRestModel.getPassword() != null && !userRestModel.getPassword().equals(""))
                editedUser.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));
            UserRestModel restUser = new UserRestModel(userRepository.save(editedUser));
            historyService.saveToHistory(editedUser, currentUser, OperationTypeEnum.UPDATE);
            return restUser;

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_EDIT_USER.getMessage());

    }

    public List<HistoryRestModel> getHistory(String token, Integer idUser) {

        User user = userRepository.findById(idUser).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND.getMessage())
        );

        User currentUser = jwtHelper.getUserFromToken(token);

        if(user.equals(currentUser) || jwtHelper.getUserFromToken(token).getRole().getRoleName().equals(RoleEnum.ADMIN) )
            return historyService.getFromHistory(user, HistoryElementEnum.USER);
        else
            throw new NotAuthorizedActionException(NOT_AUTHORIZED.getMessage());

    }

    public void delete(Integer idUser, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);
           if (jwtHelper.getUserFromToken(token).getRole().getRoleName().equals(RoleEnum.ADMIN)) {
            if (userRepository.existsUserByIdUser(idUser)) {

                userRepository.findById(idUser)
                        .map(user -> {
                            onUserDeleting(currentUser, user);
                            user.setDeleted(true);
                            UserRestModel restUser = new UserRestModel(userRepository.save(user));
                            historyService.saveToHistory(user, currentUser, OperationTypeEnum.DELETE);
                            return restUser;
                        });

            } else
                throw new ResourceNotFoundException(USER_NOT_FOUND.getMessage());

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_EDIT_USER.getMessage());


    }

    public List<UserRestModel> getInactiveUsers(String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        if(!currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN))
            throw new NotAuthorizedActionException(CANNOT_GET_INACTIVE_USERS.getMessage());

        return userRepository.findAll().stream()
                .filter(it -> it.getActive().equals(false))
                .map(UserRestModel::new)
                .collect(Collectors.toList());
    }

}


