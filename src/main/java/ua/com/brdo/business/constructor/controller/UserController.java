package ua.com.brdo.business.constructor.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua.com.brdo.business.constructor.model.User;

@RequestMapping("/api")
@RestController
public class UserController {

    @RequestMapping(method = RequestMethod.GET)
    public String adminPage(@AuthenticationPrincipal User user) {
        return "You are loged in as:  " + user.getUsername();
    }
}
