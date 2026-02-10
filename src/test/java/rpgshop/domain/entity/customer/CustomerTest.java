package rpgshop.domain.entity.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTest {
    @Test
    void shouldCreateCustomer() {
        final UUID id = UUID.randomUUID();
        final Gender gender = Gender.MALE;
        final String name = "John Doe";
        final LocalDate dateOfBirth = LocalDate.of(1990, 1, 15);
        final String cpf = "12345678901";
        final String email = "john@example.com";
        final String password = "hashedPassword";
        final BigDecimal ranking = new BigDecimal("4.5");
        final String customerCode = "CUST001";
        final List<Phone> phones = List.of();
        final List<Address> addresses = List.of();
        final List<CreditCard> creditCards = List.of();
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Customer customer = Customer.builder()
            .id(id)
            .gender(gender)
            .name(name)
            .dateOfBirth(dateOfBirth)
            .cpf(cpf)
            .email(email)
            .password(password)
            .ranking(ranking)
            .customerCode(customerCode)
            .phones(phones)
            .addresses(addresses)
            .creditCards(creditCards)
            .isActive(isActive)
            .deactivatedAt(null)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, customer.id());
        assertEquals(gender, customer.gender());
        assertEquals(name, customer.name());
        assertEquals(dateOfBirth, customer.dateOfBirth());
        assertEquals(cpf, customer.cpf());
        assertEquals(email, customer.email());
        assertEquals(password, customer.password());
        assertEquals(ranking, customer.ranking());
        assertEquals(customerCode, customer.customerCode());
        assertEquals(phones, customer.phones());
        assertEquals(addresses, customer.addresses());
        assertEquals(creditCards, customer.creditCards());
        assertTrue(customer.isActive());
        assertNull(customer.deactivatedAt());
        assertEquals(createdAt, customer.createdAt());
        assertEquals(updatedAt, customer.updatedAt());
    }

    @Test
    void shouldCreateCustomerWithAllGenderTypes() {
        for (Gender gender : Gender.values()) {
            final Customer customer = Customer.builder().gender(gender).build();
            assertEquals(gender, customer.gender());
        }
    }

    @Test
    void shouldCreateCustomerWithNullValues() {
        final Customer customer = Customer.builder().build();

        assertNull(customer.id());
        assertNull(customer.gender());
        assertNull(customer.name());
        assertNull(customer.dateOfBirth());
        assertNull(customer.cpf());
        assertNull(customer.email());
        assertNull(customer.password());
        assertNull(customer.ranking());
        assertNull(customer.customerCode());
        assertNull(customer.phones());
        assertNull(customer.addresses());
        assertNull(customer.creditCards());
        assertFalse(customer.isActive());
        assertNull(customer.deactivatedAt());
        assertNull(customer.createdAt());
        assertNull(customer.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "John Doe";
        final String newName = "Jane Doe";

        final Customer original = Customer.builder().name(originalName).build();
        final Customer modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

