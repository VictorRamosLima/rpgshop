package rpgshop.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "rpgshop.product.auto-deactivation")
public class ProductAutoDeactivationProperties {
    private boolean enabled = true;
    private BigDecimal threshold = new BigDecimal("50.00");
}
