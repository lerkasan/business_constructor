package ua.com.brdo.business.constructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import javax.validation.Valid;

import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.service.UserService;

@Controller
@RequestMapping(path = "/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = {RequestMethod.GET})
    public String register_GET() {
        return "signup.html";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register_POST(@Valid @RequestBody UserDto userDto) {
        User user = userService.registerUser(userDto);

        URI location = ServletUriComponentsBuilder.fromUriString("users").path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(user);

    }

}
