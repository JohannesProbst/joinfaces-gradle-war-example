package org.joinfaces.example;

import jakarta.inject.Named;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lars Grefer
 */
@SpringBootApplication
@ComponentScan(basePackages = YourSpringConfiguration.BEANS_PACKAGE, excludeFilters = @ComponentScan.Filter(
    Named.class))
public class WarExampleApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WarExampleApplication.class, args);
    }
}
