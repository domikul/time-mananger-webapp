package pl.wasko.time.manager.rest.api.model.response;


import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;

public class EmailRestModel {

    private Integer idEmail;
    private String emailName;

    public EmailRestModel() {
    }

    public EmailRestModel(Email email) {
        this.idEmail = email.getIdEmail();
        this.emailName = email.getEmailName();
    }

    public Integer getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(Integer idEmail) {
        this.idEmail = idEmail;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }
}
