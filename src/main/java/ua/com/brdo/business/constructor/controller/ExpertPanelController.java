package ua.com.brdo.business.constructor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXPERT')")
@RequestMapping("/app")
@RestController
public class ExpertPanelController {

    @RequestMapping(value = "/expertpanel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpertPanelResponse> adminPage(Principal principal){
        return new ResponseEntity<ExpertPanelResponse>(
                new ExpertPanelResponse("You are loged in page ExpertPanel as:  " + principal.getName() + "!"), HttpStatus.OK);
    }

    public static class ExpertPanelResponse {
        private String message;

        public ExpertPanelResponse(String message) {
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
