package ua.com.brdo.business.constructor.config;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ua.com.brdo.business.constructor.utils.UserDetailsServiceImpl;
import ua.com.brdo.business.constructor.utils.restsecurity.RESTAuthenticationEntryPoint;
import ua.com.brdo.business.constructor.utils.restsecurity.RESTAuthenticationFailureHandler;
import ua.com.brdo.business.constructor.utils.restsecurity.RESTAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index/*", "/register/*").permitAll()
                .antMatchers("/adminpanel**").hasRole("ADMIN")
                .antMatchers("/expertpanel**").hasRole("EXPERT")
                .antMatchers("/app/**").authenticated();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.formLogin().successHandler(authenticationSuccessHandler);
        http.formLogin().failureHandler(authenticationFailureHandler);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
