package ait.cohort5860.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

@Configuration // @Component - configuration
public class ServiceConfiguration {

    // Spring will call the method and put what it returns in the application context
    // @Component - only over classes
    // @Bean - only over methods
    @Bean
    ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)   // fill fields without setters
                .setFieldAccessLevel(AccessLevel.PRIVATE) // право изменять private поля
                .setMatchingStrategy(MatchingStrategies.STRICT); // the right to modify private fields
        return mapper;
    }
}
