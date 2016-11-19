package ua.com.brdo.business.constructor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/app")
@RestController
public class AdminPanelController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/adminpanel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String adminPage(Principal principal) {
        return "You are loged in page AdminPanel as:  " + principal.getName();
    }
}
