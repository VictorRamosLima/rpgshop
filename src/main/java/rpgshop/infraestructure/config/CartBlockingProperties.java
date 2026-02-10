package rpgshop.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "rpgshop.cart.blocking")
public class CartBlockingProperties {
    private int timeoutMinutes = 30;
    private boolean autoReleaseEnabled = true;
}
