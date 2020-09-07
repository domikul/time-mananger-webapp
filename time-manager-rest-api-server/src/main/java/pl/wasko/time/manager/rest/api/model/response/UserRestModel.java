package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.enumeration.RoleEnum;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserRestModel {

    private Integer idUser;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Integer roleId;

    private Integer positionId;

    private Boolean active;

    public UserRestModel(Integer idUser, String email, String password, String firstName, String lastName, Integer roleId, Integer positionId, Boolean active) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.positionId = positionId;
        this.active = active;
    }

    //testing
    public UserRestModel( String email, String password, String firstName, String lastName, Integer roleId, Integer positionId) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.positionId = positionId;
    }

    public UserRestModel(User user) {
        this.idUser = user.getIdUser();
        this.email = user.getEmail().getEmailName();
        this.password = null;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roleId = user.getRole().getIdRole();
        this.positionId = user.getPositionId();
        this.active = user.getActive();
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
