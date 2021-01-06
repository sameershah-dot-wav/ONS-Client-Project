package ons.group8.services;

import ons.group8.domain.ChecklistTemplate;
import ons.group8.domain.PersonalChecklist;
import ons.group8.domain.Role;
import ons.group8.domain.User;
import ons.group8.repositories.ChecklistTemplateRepositoryJPA;
import ons.group8.repositories.PersonalChecklistRepositoryJPA;
import ons.group8.repositories.RoleRepositoryJPA;
import ons.group8.repositories.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    private UserRepositoryJPA userRepositoryJPA;
    private RoleRepositoryJPA roleRepositoryJPA;
    private PersonalChecklistRepositoryJPA personalChecklistRepositoryJPA;
    private ChecklistTemplateRepositoryJPA checklistTemplateRepositoryJPA;

    @Autowired
    public AdminServiceImpl(UserRepositoryJPA aUserRepositoryJPA, RoleRepositoryJPA aRoleRepositoryJPA,
                            PersonalChecklistRepositoryJPA aPersonalChecklistRepositoryJPA, ChecklistTemplateRepositoryJPA aChecklistTemplateRepositoryJPA) {
        userRepositoryJPA = aUserRepositoryJPA;
        roleRepositoryJPA = aRoleRepositoryJPA;
        personalChecklistRepositoryJPA = aPersonalChecklistRepositoryJPA;
        checklistTemplateRepositoryJPA = aChecklistTemplateRepositoryJPA;
    }

    @Override
    public List<User> findAll() {
        System.out.print(userRepositoryJPA.findAll());
        return userRepositoryJPA.findAll();
    }

    @Override
    public List<PersonalChecklist> findPersonalChecklistsByUser_Id(Long id) {
        return personalChecklistRepositoryJPA.findPersonalChecklistsByUser_Id(id);
    }

    @Override
    public List<ChecklistTemplate> findChecklistTemplatesByAuthor_Id(Long id){
        return checklistTemplateRepositoryJPA.findChecklistTemplatesByAuthor_Id(id);
    }

    @Override
    public Long findUsersIdByEmail(String email) {
        return userRepositoryJPA.findUsersByEmail(email).get().getId();
    }

    @Override
    public Optional<User> findUsersByEmail(String email) {
        return userRepositoryJPA.findUsersByEmail(email);
    }

    @Override
    public Optional<Role> findRolesById(Long id) {
        return roleRepositoryJPA.findById(id);
    }

    @Override
    public void deleteUserById(Long id) {}

    @Override
    public User findUserById(Long id) {
        return userRepositoryJPA.findUserById(id);
    }
}
