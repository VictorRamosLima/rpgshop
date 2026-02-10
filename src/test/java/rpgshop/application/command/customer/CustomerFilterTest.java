package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.Gender;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerFilterTest {
    @Test
    void shouldCreateCustomerFilter() {
        final String name = "John";
        final String cpf = "12345678901";
        final String email = "john@example.com";
        final String customerCode = "CUST001";
        final Gender gender = Gender.MALE;
        final Boolean isActive = true;

        final CustomerFilter filter = new CustomerFilter(
            name,
            cpf,
            email,
            customerCode,
            gender,
            isActive
        );

        assertEquals(name, filter.name());
        assertEquals(cpf, filter.cpf());
        assertEquals(email, filter.email());
        assertEquals(customerCode, filter.customerCode());
        assertEquals(gender, filter.gender());
        assertEquals(isActive, filter.isActive());
    }
}

