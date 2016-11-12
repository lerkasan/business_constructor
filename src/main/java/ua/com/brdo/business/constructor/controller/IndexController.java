package ua.com.brdo.business.constructor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @RequestMapping(path = "/register", method = {GET})
    public String registerGet() {
        return "signup.html";
    }
}