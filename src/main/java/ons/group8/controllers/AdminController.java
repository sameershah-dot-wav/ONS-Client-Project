package ons.group8.controllers;

import lombok.extern.slf4j.Slf4j;
import ons.group8.controllers.UserRoleForm;
import ons.group8.domain.Role;
import ons.group8.domain.User;
import ons.group8.repositories.RoleRepositoryJPA;
import ons.group8.repositories.UserRepositoryJPA;
import ons.group8.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private AdminService theAdminService;
    private RoleRepositoryJPA theRoleRepositoryJPA;
    private UserRepositoryJPA theUserRepositoryJPA;


    @Autowired
    public AdminController(AdminService aAdminService, RoleRepositoryJPA aRoleRepositoryJPA, UserRepositoryJPA aUserRepositoryJPA) {
        theAdminService = aAdminService;
        theRoleRepositoryJPA = aRoleRepositoryJPA;
        theUserRepositoryJPA = aUserRepositoryJPA;
    }

    @GetMapping("user-roles")
    public String getList(Model model){
        model.addAttribute("users", theAdminService.findAll());
        return "user-roles";
    }


    @GetMapping("userrole-form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String serveUserForm(Model model) {
        UserRoleForm userRoleForm = new UserRoleForm();
        userRoleForm.setRoles(theRoleRepositoryJPA.findAll());
        model.addAttribute("user", userRoleForm);
        return "userrole-form";
    }


    @PostMapping("userrole-form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String handleUserForm(@Valid @ModelAttribute("user") UserRoleForm userRoleForm, BindingResult bindings, Model model) {
        if(bindings.hasErrors()){
            userRoleForm.setRoles(theRoleRepositoryJPA.findAll());
            System.out.println("errors = " + bindings.getAllErrors());
            return "userrole-form";
        }
        Optional<User> userExist = theAdminService.findUsersByEmail(userRoleForm.getEmail());
        if(userExist.isEmpty()) {
            log.error("user not exist");
            userRoleForm.setRoles(theRoleRepositoryJPA.findAll());
            bindings.addError(new FieldError("user","email", "email must be unique."));
            System.out.println("are there errors = " + bindings.hasErrors());
            System.out.println("errors = " + bindings.getAllErrors());
            return "userrole-form";
        }
        Set<Role> newRoles = userRoleForm
                .getRoles()
                .stream()
                .map(r -> theAdminService.findRolesById(r.getId()).get())
                .collect(Collectors.toSet());
        userExist.get().setRoles(newRoles);
        theUserRepositoryJPA.save(userExist.get());
        model.addAttribute("users", theAdminService.findAll());
        return "user-roles";
    }
}
