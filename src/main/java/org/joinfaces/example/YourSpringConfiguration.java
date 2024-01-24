package org.joinfaces.example;

import jakarta.inject.Named;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = YourSpringConfiguration.BEANS_PACKAGE, excludeFilters = @ComponentScan.Filter(
		Named.class))
public class YourSpringConfiguration implements
		WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	public static final String BEANS_PACKAGE = "org.joinfaces.example";

	@Override
	public void customize(final ConfigurableServletWebServerFactory factory) {

	}

	// ...
}
