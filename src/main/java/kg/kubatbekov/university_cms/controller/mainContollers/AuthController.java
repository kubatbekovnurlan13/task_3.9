package kg.kubatbekov.university_cms.controller.mainContollers;

import kg.kubatbekov.university_cms.model.UserEntity;
import kg.kubatbekov.university_cms.service.UserService;
import kg.kubatbekov.university_cms.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/get-users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "auth/users";
    }


    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }


    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserEntity userEntity) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") UserEntity userEntity,
                                      BindingResult bindingResult) {
        userValidator.validate(userEntity, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        userEntity.setRoles("ROLE_USER");
        userService.save(userEntity);
        return "redirect:/auth/login";
    }
}
