package ua.com.brdo.business.constructor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/app")
@RestController
public class AdminPanelController {

    @RequestMapping(value = "/adminpanel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminPanelResponse> adminPage(Principal principal){
        return new ResponseEntity<AdminPanelResponse>(
                new AdminPanelResponse("You are loged in page AdminPanel as:  " + principal.getName() + "!"), HttpStatus.OK);
    }

    public static class AdminPanelResponse {
        private String message;

        public AdminPanelResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
