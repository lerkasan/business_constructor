package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.brdo.business.constructor.service.UserService;

@RestController
@RequestMapping(path = "/api/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/available")
    public boolean isAvailableEmailGet(@RequestParam("email") String email) {
        if ((email == null)||(email.equals("")))
            return false;
        else return !userService.isEmail(email);
    }
}
