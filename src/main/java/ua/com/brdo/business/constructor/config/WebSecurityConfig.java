package ua.com.brdo.business.constructor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;
import ua.com.brdo.business.constructor.utils.restsecurity.RESTAuthenticationEntryPoint;
import ua.com.brdo.business.constructor.utils.restsecurity.RESTAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("register/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/available**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/questions/**", "/api/options/**", "/api/permits/**").hasAnyRole("USER", "EXPERT")
                .antMatchers("/api/questions/**", "/api/options/**").hasAnyRole("EXPERT")
                .antMatchers(HttpMethod.GET, "/api/laws/**").permitAll()
                .antMatchers("/api/laws/**").hasAnyRole("ADMIN", "EXPERT")
                .antMatchers("/api/**").hasAnyRole("ADMIN", "EXPERT")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/expert/**").hasRole("EXPERT")
                .antMatchers("/admin/**").hasRole("ADMIN");

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin().successHandler(authenticationSuccessHandler).failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
