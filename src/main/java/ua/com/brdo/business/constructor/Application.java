package ua.com.brdo.business.constructor;

import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.com.brdo.business.constructor.entity.Role;
import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.repositories.UsersRepository;

import java.util.*;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ua.com.brdo.business.constructor.repositories"})
@EntityScan("ua.com.brdo.business.constructor.entity") public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);

    }

}
