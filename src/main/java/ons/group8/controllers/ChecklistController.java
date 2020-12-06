package ons.group8.controllers;

import ons.group8.domain.*;
import ons.group8.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChecklistController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonalChecklistService personalChecklistService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private ChecklistTemplateItemService checklistTemplateItemService;

    @Autowired
    private ChecklistTemplateService checklistTemplateService;



    @GetMapping("view-checklist-starter")
    public String viewChecklist(@RequestParam("email") String email, Model model){

        User user = userService.findByEmail(email);
        PersonalChecklist personalChecklist = personalChecklistService.findByUserId_Id(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("personalChecklist", personalChecklist);

        return "viewChecklistStarter";
    }
}