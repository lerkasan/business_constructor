package ua.com.brdo.business.constructor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ua.com.brdo.business.constructor.entity.User;

import javax.validation.Valid;
import java.security.Principal;

@RestController public class loginController extends WebMvcConfigurerAdapter {

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginForm(User user){
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userValidation(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "{error : \"" + bindingResult.getFieldError().toString() + "\"}";
        }
        return "redirect:/secret";
    }
}
