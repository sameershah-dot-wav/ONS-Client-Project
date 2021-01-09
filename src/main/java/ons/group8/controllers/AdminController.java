package ons.group8.controllers;

import lombok.extern.slf4j.Slf4j;
import ons.group8.controllers.forms.UserRoleForm;
import ons.group8.domain.PersonalChecklist;
import ons.group8.domain.Role;
import ons.group8.domain.User;
import ons.group8.repositories.ConfirmationTokenRepositoryJPA;
import ons.group8.repositories.PersonalChecklistRepositoryJPA;
import ons.group8.repositories.RoleRepositoryJPA;
import ons.group8.repositories.UserRepositoryJPA;
import ons.group8.services.AdminService;
import ons.group8.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final AdminService theAdminService;
    private final RoleRepositoryJPA theRoleRepositoryJPA;
    private final UserRepositoryJPA theUserRepositoryJPA;
    private final PersonalChecklistRepositoryJPA thePersonalChecklistRepositoryJPA;
    private final ConfirmationTokenRepositoryJPA theConfirmationTokenRepositoryJPA;
    private final UserService userService;


    @Autowired
    public AdminController(AdminService aAdminService,
                           RoleRepositoryJPA aRoleRepositoryJPA,
                           UserRepositoryJPA aUserRepositoryJPA,
                           UserService aUserService,
                           PersonalChecklistRepositoryJPA aPersonalChecklistRepositoryJPA,
                           ConfirmationTokenRepositoryJPA aConfirmationTokenRepositoryJPA) {
        theAdminService = aAdminService;
        theRoleRepositoryJPA = aRoleRepositoryJPA;
        theUserRepositoryJPA = aUserRepositoryJPA;
        userService = aUserService;
        thePersonalChecklistRepositoryJPA = aPersonalChecklistRepositoryJPA;
        theConfirmationTokenRepositoryJPA = aConfirmationTokenRepositoryJPA;
    }



    @GetMapping("user-roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getList(@RequestParam(value = "firstName", required = false) String searchName,Model model) {
        if (searchName == null ) {
            model.addAttribute("users", theAdminService.findAll());
            return "user-roles";
        } else {

            Set<User> users = userService.findUsersByFirstName(searchName);
            model.addAttribute("users", users);
            model.addAttribute("noUsersFound", users.isEmpty());
            return "user-roles";
        }
    }

    @GetMapping("userrole-form/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String serveUserForm(@PathVariable("userId") Long userId, Model model) {
        Optional<User> userExist = userService.findById(userId);
        if (userExist.isPresent()) {
            Set<Long> userRoles = userExist.get().getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            UserRoleForm userRoleForm = new UserRoleForm(userExist.get(), userRoles);
            model.addAttribute("userRoleForm", userRoleForm);
            model.addAttribute("allRoles", theRoleRepositoryJPA.findAll());

            return "userrole-form";
        } else {
            log.error("Could not user with id: " + userId + " while trying to serve user role form");
            model.addAttribute("title", "404 - Not found");
            model.addAttribute("message", "The requested user was not found.");
            return "message";
        }
    }

    @PostMapping("userrole-form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String handleUserForm(@Valid @ModelAttribute("userRoleForm") UserRoleForm userRoleForm, BindingResult bindings, Model model) {
        if(bindings.hasErrors()){
            model.addAttribute("allRoles", theRoleRepositoryJPA.findAll());
            System.out.println("errors = " + bindings.getAllErrors());
            return "userrole-form";
        }
        User userExist = userRoleForm.getUser();

        if(userExist == null) {
            log.error("user not exist");
            model.addAttribute("allRoles", theRoleRepositoryJPA.findAll());
            System.out.println("are there errors = " + bindings.hasErrors());
            System.out.println("errors = " + bindings.getAllErrors());
            return "userrole-form";
        }

        if(userExist.isEnabled() == false){
            log.error("details of unverified users cannot be changed");
            model.addAttribute("message", "The email address of" + userExist.getFirstName() + " " + userExist.getLastName() + " has not been verified");
            model.addAttribute("allRoles", theRoleRepositoryJPA.findAll());
            return "userrole-form";
        }


        Set<Role> newRoles = userRoleForm
                .getAssignedRolesIds()
                .stream()
                .map(r -> theAdminService.findRolesById(r).get())
                .collect(Collectors.toSet());
        userExist.setRoles(newRoles);

        theUserRepositoryJPA.save(userExist);
        model.addAttribute("users", theAdminService.findAll());
        return "user-roles";
    }

    @GetMapping("/user-delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        List<PersonalChecklist> personalChecklistList = thePersonalChecklistRepositoryJPA.findPersonalChecklistsByUser_Id(id);
        thePersonalChecklistRepositoryJPA.deleteAll(personalChecklistList);
        theConfirmationTokenRepositoryJPA.delete(theConfirmationTokenRepositoryJPA.findByUserId(id));
        theUserRepositoryJPA.delete(theUserRepositoryJPA.findUserById(id));

         model.addAttribute("users", theAdminService.findAll());
        return "user-roles";
    }
}