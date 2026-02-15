package rpgshop.domain.entity.customer;

import lombok.Builder;
import rpgshop.domain.entity.customer.constant.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record Customer(
    UUID id,
    Gender gender,
    String name,
    LocalDate dateOfBirth,
    String cpf,
    String email,
    BigDecimal ranking,
    String customerCode,
    List<Phone> phones,
    List<Address> addresses,
    List<CreditCard> creditCards,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
