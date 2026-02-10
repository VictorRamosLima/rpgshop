package rpgshop.application.usecase.coupon;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.coupon.CreateCouponCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.customer.Customer;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateCouponUseCase {
    private final CouponGateway couponGateway;
    private final CustomerGateway customerGateway;

    public CreateCouponUseCase(final CouponGateway couponGateway, final CustomerGateway customerGateway) {
        this.couponGateway = couponGateway;
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Coupon execute(@Nonnull final CreateCouponCommand command) {
        if (command.code() == null || command.code().isBlank()) {
            throw new BusinessRuleException("Coupon code is required");
        }
        if (command.type() == null) {
            throw new BusinessRuleException("Coupon type is required");
        }
        if (command.value() == null || command.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Coupon value must be greater than zero");
        }
        if (couponGateway.existsByCode(command.code())) {
            throw new BusinessRuleException("Coupon code '%s' already exists".formatted(command.code()));
        }

        Customer customer = null;
        if (command.customerId() != null) {
            customer = customerGateway.findById(command.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));
        }

        final Coupon coupon = Coupon.builder()
            .id(UUID.randomUUID())
            .code(command.code())
            .type(command.type())
            .value(command.value())
            .customer(customer)
            .isUsed(false)
            .expiresAt(command.expiresAt())
            .build();

        return couponGateway.save(coupon);
    }
}
