package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.Role;

public class RoleRestModel {

    private Integer idRole;
    private String roleName;
    private Integer maxBuckets;

    public RoleRestModel() {
    }

    public RoleRestModel(Role role) {
        this.idRole = role.getIdRole();
        this.roleName = role.getRoleName().name();
        this.maxBuckets = role.getMaxBuckets();
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getMaxBuckets() {
        return maxBuckets;
    }

    public void setMaxBuckets(Integer maxBuckets) {
        this.maxBuckets = maxBuckets;
    }
}
