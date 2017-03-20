package ch.eike.springer.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Tanemahuta on 18.03.17.
 */
@Configuration
@ComponentScan(basePackages = "ch.eike.springer.services")
public class ServiceConfiguration {
    // NOTe Intentionally empty, this is a configuration using the component scan to find all services by the convention package
}
