package ua.com.brdo.business.constructor.utils.restsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class RESTAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.addHeader("WWWW-Authenticate:", "Basic realm=\"" + getRealmName() + "\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("BasicConstructorRealm");
        super.afterPropertiesSet();
    }
}
