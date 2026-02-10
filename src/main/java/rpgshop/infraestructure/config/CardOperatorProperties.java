package rpgshop.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "rpgshop.payment-operator")
public class CardOperatorProperties {
    private boolean enabled = true;
    private BigDecimal maxAmountPerCard = new BigDecimal("5000.00");
    private List<String> rejectedCardSuffixes = new ArrayList<>(List.of("0000"));
}
