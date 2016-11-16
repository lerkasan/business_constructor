package ua.com.brdo.business.constructor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXPERT' ,'ROLE_USER')")
@RequestMapping("/app")
@RestController()
public class SomeController {

    @RequestMapping(value = "/someurl", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SomeResponse> hello(Principal principal) {

        return new ResponseEntity<SomeResponse>(
                new SomeResponse("You are loged in page some as:  " + principal.getName() + "!"), HttpStatus.OK);
    }

    public static class SomeResponse {
        private String message;

        public SomeResponse(String message) {
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
