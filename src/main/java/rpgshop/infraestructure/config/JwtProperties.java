package rpgshop.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "rpgshop.jwt")
public class JwtProperties {
    private String secret;
    private int expirationHours = 24;
}
