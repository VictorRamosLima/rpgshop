package rpgshop.presentation.controller.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.customer.CreateCreditCardCommand;
import rpgshop.application.command.order.CreateOrderCommand;
import rpgshop.application.command.order.PaymentInfo;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.usecase.customer.CreateCreditCardUseCase;
import rpgshop.application.usecase.order.ApproveOrderUseCase;
import rpgshop.application.usecase.order.CreateOrderUseCase;
import rpgshop.application.usecase.order.DeliverOrderUseCase;
import rpgshop.application.usecase.order.DispatchOrderUseCase;
import rpgshop.application.usecase.order.QueryOrdersUseCase;
import rpgshop.application.usecase.order.RejectOrderUseCase;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public final class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final ApproveOrderUseCase approveOrderUseCase;
    private final RejectOrderUseCase rejectOrderUseCase;
    private final DispatchOrderUseCase dispatchOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;
    private final QueryOrdersUseCase queryOrdersUseCase;
    private final AddressGateway addressGateway;
    private final CreditCardGateway creditCardGateway;
    private final CouponGateway couponGateway;
    private final CardBrandGateway cardBrandGateway;
    private final CreateCreditCardUseCase createCreditCardUseCase;
    private final CustomerGateway customerGateway;

    public OrderController(
        final CreateOrderUseCase createOrderUseCase,
        final ApproveOrderUseCase approveOrderUseCase,
        final RejectOrderUseCase rejectOrderUseCase,
        final DispatchOrderUseCase dispatchOrderUseCase,
        final DeliverOrderUseCase deliverOrderUseCase,
        final QueryOrdersUseCase queryOrdersUseCase,
        final AddressGateway addressGateway,
        final CreditCardGateway creditCardGateway,
        final CouponGateway couponGateway,
        final CardBrandGateway cardBrandGateway,
        final CreateCreditCardUseCase createCreditCardUseCase,
        final CustomerGateway customerGateway
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.approveOrderUseCase = approveOrderUseCase;
        this.rejectOrderUseCase = rejectOrderUseCase;
        this.dispatchOrderUseCase = dispatchOrderUseCase;
        this.deliverOrderUseCase = deliverOrderUseCase;
        this.queryOrdersUseCase = queryOrdersUseCase;
        this.addressGateway = addressGateway;
        this.creditCardGateway = creditCardGateway;
        this.couponGateway = couponGateway;
        this.cardBrandGateway = cardBrandGateway;
        this.createCreditCardUseCase = createCreditCardUseCase;
        this.customerGateway = customerGateway;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) final OrderStatus status,
        @RequestParam(defaultValue = "0") final int page,
        final Model model
    ) {
        final Page<Order> orders = status != null
            ? queryOrdersUseCase.findByStatus(status, PageRequest.of(page, 10))
            : queryOrdersUseCase.findAll(PageRequest.of(page, 10));

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "order/list";
    }

    @GetMapping("/customer/{customerId}")
    public String listByCustomer(
        @PathVariable final UUID customerId,
        @RequestParam(defaultValue = "0") final int page,
        Model model
    ) {
        final Page<Order> orders = queryOrdersUseCase.findByCustomerId(customerId, PageRequest.of(page, 10));
        model.addAttribute("orders", orders);
        model.addAttribute("customerId", customerId);
        model.addAttribute("statuses", OrderStatus.values());
        return "order/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable final UUID id, final Model model) {
        final var order = queryOrdersUseCase.findById(id);
        if (order.isEmpty()) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order.get());
        return "order/detail";
    }

    @GetMapping("/checkout/{customerId}")
    public String showCheckout(@PathVariable final UUID customerId, final Model model) {
        if (customerGateway.findById(customerId).isEmpty()) {
            return "redirect:/customers";
        }

        prepareCheckoutModel(customerId, model);
        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(
        @RequestParam UUID customerId,
        @RequestParam(defaultValue = "existing") final String addressMode,
        @RequestParam(required = false) final String deliveryAddressId,
        @RequestParam(required = false) final String newAddressLabel,
        @RequestParam(required = false) final String newAddressResidenceType,
        @RequestParam(required = false) final String newAddressStreetType,
        @RequestParam(required = false) final String newAddressStreet,
        @RequestParam(required = false) final String newAddressNumber,
        @RequestParam(required = false) final String newAddressNeighborhood,
        @RequestParam(required = false) final String newAddressZipCode,
        @RequestParam(required = false) final String newAddressCity,
        @RequestParam(required = false) final String newAddressState,
        @RequestParam(required = false) final String newAddressCountry,
        @RequestParam(required = false) final String newAddressObservations,
        @RequestParam(defaultValue = "false") boolean saveNewAddressToProfile,
        @RequestParam(required = false, name = "paymentCreditCardId") final List<String> paymentCreditCardIds,
        @RequestParam(required = false, name = "paymentCouponId") final List<String> paymentCouponIds,
        @RequestParam(required = false, name = "paymentAmount") final List<String> paymentAmounts,
        @RequestParam(defaultValue = "false") final boolean useNewCard,
        @RequestParam(required = false) final String newCardNumber,
        @RequestParam(required = false) final String newCardPrintedName,
        @RequestParam(required = false) final String newCardBrandId,
        @RequestParam(required = false) final String newCardSecurityCode,
        @RequestParam(defaultValue = "false") final boolean newCardPreferred,
        @RequestParam(required = false) final String newCardAmount,
        @RequestParam(required = false) final String newCardCouponId,
        final Model model
    ) {
        try {
            final UUID resolvedAddressId = resolveDeliveryAddressId(
                customerId,
                addressMode,
                deliveryAddressId,
                newAddressLabel,
                newAddressResidenceType,
                newAddressStreetType,
                newAddressStreet,
                newAddressNumber,
                newAddressNeighborhood,
                newAddressZipCode,
                newAddressCity,
                newAddressState,
                newAddressCountry,
                newAddressObservations,
                saveNewAddressToProfile
            );

            final List<PaymentInfo> payments = buildPaymentInfos(
                customerId,
                paymentCreditCardIds,
                paymentCouponIds,
                paymentAmounts,
                useNewCard,
                newCardNumber,
                newCardPrintedName,
                newCardBrandId,
                newCardSecurityCode,
                newCardPreferred,
                newCardAmount,
                newCardCouponId
            );

            final var command = new CreateOrderCommand(customerId, resolvedAddressId, payments);
            final Order order = createOrderUseCase.execute(command);
            return "redirect:/orders/" + order.id();
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            prepareCheckoutModel(customerId, model);
            return "order/checkout";
        }
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable final UUID id, final Model model) {
        try {
            approveOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable final UUID id, final Model model) {
        try {
            rejectOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/dispatch")
    public String dispatch(@PathVariable final UUID id, final Model model) {
        try {
            dispatchOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/deliver")
    public String deliver(@PathVariable final UUID id, final Model model) {
        try {
            deliverOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    private void prepareCheckoutModel(final UUID customerId, final Model model) {
        final List<Address> deliveryAddresses = addressGateway.findByCustomerIdAndPurpose(customerId, AddressPurpose.DELIVERY);
        final List<Address> checkoutAddresses = deliveryAddresses.isEmpty()
            ? addressGateway.findActiveByCustomerId(customerId)
            : deliveryAddresses;
        final List<CreditCard> creditCards = creditCardGateway.findActiveByCustomerId(customerId);
        final List<Coupon> coupons = couponGateway.findAvailableByCustomerId(customerId, Instant.now());
        final List<CardBrand> cardBrands = cardBrandGateway.findActive();

        model.addAttribute("customerId", customerId);
        model.addAttribute("deliveryAddresses", checkoutAddresses);
        model.addAttribute("creditCards", creditCards);
        model.addAttribute("availableCoupons", coupons);
        model.addAttribute("cardBrands", cardBrands);
        model.addAttribute("residenceTypes", ResidenceType.values());
        model.addAttribute("streetTypes", StreetType.values());
    }

    private UUID resolveDeliveryAddressId(
        final UUID customerId,
        final String addressMode,
        final String existingDeliveryAddressId,
        final String newAddressLabel,
        final String newAddressResidenceType,
        final String newAddressStreetType,
        final String newAddressStreet,
        final String newAddressNumber,
        final String newAddressNeighborhood,
        final String newAddressZipCode,
        final String newAddressCity,
        final String newAddressState,
        final String newAddressCountry,
        final String newAddressObservations,
        final boolean saveNewAddressToProfile
    ) {
        if (!"new".equalsIgnoreCase(addressMode)) {
            final UUID parsedAddressId = parseRequiredUuid(existingDeliveryAddressId, "Endereco de entrega");
            if (!addressGateway.existsByIdAndCustomerId(parsedAddressId, customerId)) {
                throw new BusinessRuleException("O endereco selecionado nao pertence ao cliente");
            }
            return parsedAddressId;
        }

        final ResidenceType residenceType = parseEnum(newAddressResidenceType, ResidenceType.class, "Tipo de residencia");
        final StreetType streetType = parseEnum(newAddressStreetType, StreetType.class, "Tipo de logradouro");
        final String street = requireText(newAddressStreet, "Logradouro");
        final String number = requireText(newAddressNumber, "Numero");
        final String neighborhood = requireText(newAddressNeighborhood, "Bairro");
        final String zipCode = requireText(newAddressZipCode, "CEP");
        final String city = requireText(newAddressCity, "Cidade");
        final String state = requireText(newAddressState, "Estado");
        final String country = requireText(newAddressCountry, "Pais");

        final Address createdAddress = Address.builder()
            .id(UUID.randomUUID())
            .purpose(AddressPurpose.DELIVERY)
            .label(normalize(newAddressLabel))
            .residenceType(residenceType)
            .streetType(streetType)
            .street(street)
            .number(number)
            .neighborhood(neighborhood)
            .zipCode(zipCode)
            .city(city)
            .state(state)
            .country(country)
            .observations(normalize(newAddressObservations))
            .isActive(saveNewAddressToProfile)
            .deactivatedAt(saveNewAddressToProfile ? null : Instant.now())
            .build();

        if (saveNewAddressToProfile) {
            return addressGateway.save(createdAddress, customerId).id();
        }

        return addressGateway.saveDetached(createdAddress).id();
    }

    private List<PaymentInfo> buildPaymentInfos(
        final UUID customerId,
        final List<String> paymentCreditCardIds,
        final List<String> paymentCouponIds,
        final List<String> paymentAmounts,
        final boolean useNewCard,
        final String newCardNumber,
        final String newCardPrintedName,
        final String newCardBrandId,
        final String newCardSecurityCode,
        final boolean newCardPreferred,
        final String newCardAmount,
        final String newCardCouponId
    ) {
        final List<PaymentInfo> payments = new ArrayList<>();

        final int rowCount = Math.max(
            Math.max(sizeOf(paymentCreditCardIds), sizeOf(paymentCouponIds)),
            sizeOf(paymentAmounts)
        );

        for (int i = 0; i < rowCount; i++) {
            final String amountRaw = valueAt(paymentAmounts, i);
            final String cardRaw = valueAt(paymentCreditCardIds, i);
            final String couponRaw = valueAt(paymentCouponIds, i);

            if (isBlank(amountRaw) && isBlank(cardRaw) && isBlank(couponRaw)) {
                continue;
            }

            final UUID creditCardId = parseOptionalUuid(cardRaw, "Cartao de credito");
            final UUID couponId = parseOptionalUuid(couponRaw, "Cupom");

            if (creditCardId == null && couponId == null) {
                throw new BusinessRuleException("Cada pagamento deve informar cartao, cupom ou ambos");
            }

            final BigDecimal amount = creditCardId != null
                ? parseRequiredAmount(amountRaw, "Valor do pagamento com cartao")
                : parseOptionalAmount(amountRaw, "Valor do pagamento");

            payments.add(new PaymentInfo(creditCardId, couponId, amount));
        }

        if (useNewCard) {
            final String cardNumber = requireText(newCardNumber, "Numero do novo cartao");
            final String printedName = requireText(newCardPrintedName, "Nome impresso do novo cartao");
            final UUID cardBrandId = parseRequiredUuid(newCardBrandId, "Bandeira do novo cartao");
            final String securityCode = requireText(newCardSecurityCode, "Codigo de seguranca do novo cartao");
            final BigDecimal amount = parseRequiredAmount(newCardAmount, "Valor do novo cartao");
            final UUID couponId = parseOptionalUuid(newCardCouponId, "Cupom do novo cartao");

            final CreditCard createdCard = createCreditCardUseCase.execute(new CreateCreditCardCommand(
                customerId,
                cardNumber,
                printedName,
                cardBrandId,
                securityCode,
                newCardPreferred
            ));

            payments.add(new PaymentInfo(createdCard.id(), couponId, amount));
        }

        if (payments.isEmpty()) {
            throw new BusinessRuleException("Informe ao menos uma forma de pagamento");
        }

        return payments;
    }

    private int sizeOf(final List<String> values) {
        return values == null ? 0 : values.size();
    }

    private String valueAt(final List<String> values, final int index) {
        if (values == null || index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    private UUID parseRequiredUuid(final String value, final String fieldName) {
        if (isBlank(value)) {
            throw new BusinessRuleException(fieldName + " e obrigatorio");
        }

        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(fieldName + " invalido");
        }
    }

    private UUID parseOptionalUuid(final String value, final String fieldName) {
        if (isBlank(value)) {
            return null;
        }

        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(fieldName + " invalido");
        }
    }

    private BigDecimal parseRequiredAmount(final String value, final String fieldName) {
        if (isBlank(value)) {
            throw new BusinessRuleException(fieldName + " e obrigatorio");
        }

        try {
            final BigDecimal amount = new BigDecimal(value.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleException(fieldName + " deve ser maior que zero");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new BusinessRuleException(fieldName + " invalido");
        }
    }

    private BigDecimal parseOptionalAmount(final String value, final String fieldName) {
        if (isBlank(value)) {
            return null;
        }

        try {
            final BigDecimal amount = new BigDecimal(value.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleException(fieldName + " deve ser maior que zero");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new BusinessRuleException(fieldName + " invalido");
        }
    }

    private String requireText(final String value, final String fieldName) {
        final String normalized = normalize(value);
        if (normalized == null) {
            throw new BusinessRuleException(fieldName + " e obrigatorio");
        }
        return normalized;
    }

    private <T extends Enum<T>> T parseEnum(final String value, final Class<T> enumType, final String fieldName) {
        if (isBlank(value)) {
            throw new BusinessRuleException(fieldName + " e obrigatorio");
        }

        try {
            return Enum.valueOf(enumType, value.trim());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(fieldName + " invalido");
        }
    }

    private String normalize(final String value) {
        if (value == null) {
            return null;
        }

        final String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
}
