package pl.wasko.time.manager.rest.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.response.EmailRestModel;
import pl.wasko.time.manager.rest.api.model.service.EmailService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final JwtHelper jwtHelper;

    public EmailController(final EmailService emailService, JwtHelper jwtHelper) {
        this.emailService = emailService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailRestModel> addEmail(@RequestBody final EmailRestModel emailRestModel,
                                            @RequestHeader(value = "Authorization") String token) {
        jwtHelper.getUserFromToken(token); //check if user is active
        return ResponseEntity.ok(emailService.add(emailRestModel));
    }

    @GetMapping
    public ResponseEntity<List<EmailRestModel>> listEmails(@RequestHeader(value = "Authorization") String token) {
        jwtHelper.getUserFromToken(token); //check if user is active
        final List<EmailRestModel> emailList = emailService.getAll();

        return ResponseEntity.ok(emailList);
    }

    @GetMapping(value = "/free")
    public ResponseEntity<List<EmailRestModel>> listEmailsWithoutUsers(@RequestHeader(value = "Authorization") String token) {
        jwtHelper.getUserFromToken(token); //check if user is active
        final List<EmailRestModel> emailList = emailService.getEmailsWithoutUsers();

        return ResponseEntity.ok(emailList);
    }


    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer>  deleteEmail(@PathVariable Integer idEmail,
                                                @RequestHeader(value = "Authorization") String token) {
        jwtHelper.getUserFromToken(token); //check if user is active
        emailService.deletePermanet(idEmail);
        return new ResponseEntity<>(idEmail, HttpStatus.OK);
    }

}
