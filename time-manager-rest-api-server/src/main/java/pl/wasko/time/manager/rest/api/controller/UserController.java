package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.enumeration.RoleEnum;
import pl.wasko.time.manager.rest.api.model.response.BucketRestModel;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.PositionRepository;
import pl.wasko.time.manager.rest.api.model.repository.RoleRepository;
import pl.wasko.time.manager.rest.api.model.repository.UserRepository;
import pl.wasko.time.manager.rest.api.model.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final JwtHelper jwtHelper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserRestModel>> getAllUsers(@RequestHeader(value = "Authorization") String token) {
        jwtHelper.getUserFromToken(token); //check if user is active
        final List<UserRestModel> userList = userService.getAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping(value = "/inactive")
    public ResponseEntity<List<UserRestModel>> getInactiveUsers(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(userService.getInactiveUsers(token));
    }

    @GetMapping(value = "/history/{idUser}")
    public ResponseEntity<List<HistoryRestModel>> getUserHistory(@RequestHeader(value = "Authorization") String token, @PathVariable Integer idUser) {
        final List<HistoryRestModel> historyList = userService.getHistory(token, idUser);
        return ResponseEntity.ok(historyList);
    }


    @PostMapping
    public ResponseEntity<UserRestModel> registerUser(@RequestBody UserRestModel userRestModel, @RequestHeader(value = "Authorization") String token)
            throws WrongRequestException {

        if(!jwtHelper.getUserFromToken(token).getRole().getRoleName().equals(RoleEnum.ADMIN))
            throw new NotAuthorizedActionException(INSUFFICIENT_PERMISSIONS.getMessage());

        return ResponseEntity.ok(userService.registerUser(userRestModel, token));
    }

    @PutMapping(value = "/{idUser}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRestModel> updateUser(@RequestBody UserRestModel userRestModel,
                                                @PathVariable Integer idUser,
                                                @RequestHeader(value = "Authorization") String token) {


        return ResponseEntity.ok(userService.update(userRestModel,idUser, token));
    }


    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteUser(@PathVariable Integer idUser,
                                                 @RequestHeader(value = "Authorization") String token) {

        userService.delete(idUser,token);
        return new ResponseEntity<>(idUser, HttpStatus.OK);
    }

}
