package rpgshop.application.usecase.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.coupon.CreateCouponCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseTest {
    @Mock
    private CouponGateway couponGateway;

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CreateCouponUseCase useCase;

    @Test
    void shouldCreateGlobalCouponWhenCustomerIdIsNotProvided() {
        final CreateCouponCommand command = new CreateCouponCommand(
            "PROMO10",
            CouponType.PROMOTIONAL,
            new BigDecimal("10.00"),
            null,
            Instant.now().plusSeconds(3600)
        );

        when(couponGateway.existsByCode("PROMO10")).thenReturn(false);
        when(couponGateway.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Coupon saved = useCase.execute(command);

        assertEquals("PROMO10", saved.code());
        assertEquals(new BigDecimal("10.00"), saved.value());
        assertNull(saved.customer());
    }

    @Test
    void shouldCreateCustomerCouponWhenCustomerExists() {
        final UUID customerId = UUID.randomUUID();
        final CreateCouponCommand command = new CreateCouponCommand(
            "TROCA10",
            CouponType.EXCHANGE,
            new BigDecimal("10.00"),
            customerId,
            Instant.now().plusSeconds(3600)
        );
        final Customer customer = Customer.builder().id(customerId).build();

        when(couponGateway.existsByCode("TROCA10")).thenReturn(false);
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(couponGateway.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Coupon saved = useCase.execute(command);

        assertEquals(customerId, saved.customer().id());
    }

    @Test
    void shouldThrowWhenCouponCodeAlreadyExists() {
        final CreateCouponCommand command = new CreateCouponCommand(
            "PROMO10",
            CouponType.PROMOTIONAL,
            new BigDecimal("10.00"),
            null,
            Instant.now().plusSeconds(3600)
        );
        when(couponGateway.existsByCode("PROMO10")).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
