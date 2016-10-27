package ua.com.brdo.business.constructor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(path = "/", method = {RequestMethod.GET})
        public String index() {
            return "index.html";
    }

}