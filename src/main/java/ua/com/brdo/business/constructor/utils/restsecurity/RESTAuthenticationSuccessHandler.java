package ua.com.brdo.business.constructor.utils.restsecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;

@Component
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        handle(response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User)authentication.getPrincipal();
        String userJsonDetails = jsonMapper.writeValueAsString(user);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(userJsonDetails);
    }
}
