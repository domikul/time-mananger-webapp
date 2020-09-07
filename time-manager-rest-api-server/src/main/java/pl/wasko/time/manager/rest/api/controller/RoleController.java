package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.response.BucketRestModel;
import pl.wasko.time.manager.rest.api.model.response.RoleRestModel;
import pl.wasko.time.manager.rest.api.model.service.RoleService;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/role")
    public ResponseEntity<List<RoleRestModel>> getRoles() {
        final List<RoleRestModel> roleList = roleService.getAllRoles();
        return ResponseEntity.ok(roleList);
    }

}
