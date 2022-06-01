package Matching.SouP.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@Getter
@PropertySource("classpath:/application-oauth.properties")
public class OauthConfig {
    private final Environment env;

    private static String clientId;

    public OauthConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public void client(){
        String property = env.getProperty("spring.security.oauth2.client.registration.kakao.client-id");
        clientId = property;
    }

    public static String getClientId() {
        return clientId;
    }
}
