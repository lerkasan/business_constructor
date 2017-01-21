package ua.com.brdo.business.constructor.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.service.UserService;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/api",
            produces = APPLICATION_JSON_VALUE)
    public String adminPage(@AuthenticationPrincipal User user) {
        return "You are loged in as:  " + user.getUsername();
    }

    @PostMapping("/api/users")
    public ResponseEntity createUser(@Valid @RequestBody final User user) {
        final User registeredUser = userService.create(user);
        final URI location = ServletUriComponentsBuilder
                .fromUriString("users")
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(registeredUser);
    }

    @GetMapping("/api/users/available")
    public boolean isAvailableEmailGet(@RequestParam("email") String email) {
        return userService.isEmailAvailable(email);
    }

    @GetMapping(path = "/admin/users", produces = APPLICATION_JSON_VALUE)
    public List<User> getListUsers() { return userService.findAll();}
}
