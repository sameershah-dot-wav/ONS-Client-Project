package ons.group8.services;

import ons.group8.domain.ChecklistTemplate;
import ons.group8.domain.Role;
import ons.group8.domain.User;
import ons.group8.domain.PersonalChecklist;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {

    List<PersonalChecklist> findPersonalChecklistsByUser_Id(Long id);

    List<ChecklistTemplate> findChecklistTemplatesByAuthor_Id(Long id);

    List<User> findAll();

    Long findUsersIdByEmail(String email);

    Optional<User> findUsersByEmail(String email);

    Optional<Role> findRolesById(Long id);

    void deleteUserById(Long id);

    User findUserById(Long id);


}
