package ua.com.brdo.business.constructor.utils.restsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.com.brdo.business.constructor.model.User;

@Component
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        handle(response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletResponse response, Authentication authentication) throws IOException {

        jsonMapper.setDateFormat(dateFormat);
        User user = (User) authentication.getPrincipal();
        String userJsonDetails = jsonMapper.writeValueAsString(user);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(userJsonDetails);
    }
}
