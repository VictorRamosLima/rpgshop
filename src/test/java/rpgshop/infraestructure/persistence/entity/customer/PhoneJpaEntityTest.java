package rpgshop.infraestructure.persistence.entity.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PhoneJpaEntity")
class PhoneJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final PhoneType type = PhoneType.MOBILE;
            final String areaCode = "11";
            final String number = "999887766";

            final PhoneJpaEntity entity = PhoneJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .customer(customer)
                .type(type)
                .areaCode(areaCode)
                .number(number)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(type, entity.getType());
            assertEquals(areaCode, entity.getAreaCode());
            assertEquals(number, entity.getNumber());
        }

        @Test
        @DisplayName("should create entity with all phone types")
        void shouldCreateEntityWithAllPhoneTypes() {
            for (PhoneType type : PhoneType.values()) {
                final PhoneJpaEntity entity = PhoneJpaEntity.builder()
                    .type(type)
                    .build();

                assertEquals(type, entity.getType());
            }
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final PhoneJpaEntity entity = PhoneJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final PhoneJpaEntity entity = PhoneJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getType());
            assertNull(entity.getAreaCode());
            assertNull(entity.getNumber());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final PhoneJpaEntity entity = new PhoneJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getType());
            assertNull(entity.getAreaCode());
            assertNull(entity.getNumber());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final PhoneType type = PhoneType.HOME;
            final String areaCode = "21";
            final String number = "32145678";

            final PhoneJpaEntity entity = new PhoneJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, customer, type, areaCode, number
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(type, entity.getType());
            assertEquals(areaCode, entity.getAreaCode());
            assertEquals(number, entity.getNumber());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final PhoneJpaEntity entity = new PhoneJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final PhoneType type = PhoneType.WORK;
            final String areaCode = "31";
            final String number = "987654321";

            entity.setId(id);
            entity.setActive(false);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCustomer(customer);
            entity.setType(type);
            entity.setAreaCode(areaCode);
            entity.setNumber(number);

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(type, entity.getType());
            assertEquals(areaCode, entity.getAreaCode());
            assertEquals(number, entity.getNumber());
        }
    }
}
