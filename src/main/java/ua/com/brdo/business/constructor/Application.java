package ua.com.brdo.business.constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.com.brdo.business.constructor.entity.User;

@SpringBootApplication
@ComponentScan
@EnableJpaRepositories(basePackages = {"ua.com.brdo.business.constructor.repositories"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }

}
