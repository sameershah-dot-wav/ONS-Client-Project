package ons.group8.controllers;

import ons.group8.controllers.forms.UserForm;
import ons.group8.domain.ConfirmationToken;
import ons.group8.domain.User;
import ons.group8.repositories.ConfirmationTokenRepositoryJPA;
import ons.group8.repositories.UserRepositoryJPA;
import ons.group8.services.EmailSenderService;
import ons.group8.services.UserCreationEvent;
import ons.group8.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/sign-up")
public class SignupController {

    private final UserService userService;

    @Autowired
    private UserRepositoryJPA userRepositoryJPA;

    @Autowired
    private ConfirmationTokenRepositoryJPA confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("register")
    public String addUserForm(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("create-user")
    public String addUser(@Valid UserForm newUser, User user, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            try {
                userService.save(new UserCreationEvent(newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), newUser.getPassword(), newUser.getRepeatPassword()));
                ConfirmationToken confirmationToken = new ConfirmationToken(userService.findByEmail("ShahS58@cardiff.ac.uk"));

                confirmationTokenRepository.save(confirmationToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setFrom("chand312902@gmail.com");
                mailMessage.setText("To confirm your account, please click here : "
                        +"http://localhost:8443/confirm-account?token="+confirmationToken.getConfirmationToken());

                emailSenderService.sendEmail(mailMessage);



                return "message";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "register";
            }
        }
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepositoryJPA.findUserByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userRepositoryJPA.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }
}
