package Matching.SouP.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:/application-real-db.properties")
public class PropertyUtil implements EnvironmentAware {
    private static Environment environment;

    @Override public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
}