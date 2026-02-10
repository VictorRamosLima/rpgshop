package rpgshop.infraestructure.persistence.entity.supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SupplierJpaEntity")
class SupplierJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final boolean isActive = true;
            final String name = "RPG Supplies Co.";
            final String legalName = "RPG Supplies Company LTDA";
            final String cnpj = "12345678000199";
            final String email = "contact@rpgsupplies.com";
            final String phone = "11999887766";

            final SupplierJpaEntity entity = SupplierJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .isActive(isActive)
                .deactivatedAt(null)
                .name(name)
                .legalName(legalName)
                .cnpj(cnpj)
                .email(email)
                .phone(phone)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(name, entity.getName());
            assertEquals(legalName, entity.getLegalName());
            assertEquals(cnpj, entity.getCnpj());
            assertEquals(email, entity.getEmail());
            assertEquals(phone, entity.getPhone());
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final SupplierJpaEntity entity = SupplierJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final SupplierJpaEntity entity = SupplierJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getName());
            assertNull(entity.getLegalName());
            assertNull(entity.getCnpj());
            assertNull(entity.getEmail());
            assertNull(entity.getPhone());
        }

        @Test
        @DisplayName("should create entity with minimal fields")
        void shouldCreateEntityWithMinimalFields() {
            final String name = "Simple Supplier";

            final SupplierJpaEntity entity = SupplierJpaEntity.builder()
                .name(name)
                .build();

            assertEquals(name, entity.getName());
            assertTrue(entity.isActive());
            assertNull(entity.getLegalName());
            assertNull(entity.getCnpj());
            assertNull(entity.getEmail());
            assertNull(entity.getPhone());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final SupplierJpaEntity entity = new SupplierJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getName());
            assertNull(entity.getLegalName());
            assertNull(entity.getCnpj());
            assertNull(entity.getEmail());
            assertNull(entity.getPhone());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final String name = "Inactive Supplier";
            final String legalName = "Inactive Supplier SA";
            final String cnpj = "98765432000188";
            final String email = "old@supplier.com";
            final String phone = "21988776655";

            final SupplierJpaEntity entity = new SupplierJpaEntity(
                id, createdAt, updatedAt, isActive, deactivatedAt, name, legalName, cnpj, email, phone
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(name, entity.getName());
            assertEquals(legalName, entity.getLegalName());
            assertEquals(cnpj, entity.getCnpj());
            assertEquals(email, entity.getEmail());
            assertEquals(phone, entity.getPhone());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final SupplierJpaEntity entity = new SupplierJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Instant deactivatedAt = Instant.now();
            final String name = "Updated Supplier";
            final String legalName = "Updated Supplier ME";
            final String cnpj = "11223344000155";
            final String email = "updated@supplier.com";
            final String phone = "31977665544";

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setActive(false);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setName(name);
            entity.setLegalName(legalName);
            entity.setCnpj(cnpj);
            entity.setEmail(email);
            entity.setPhone(phone);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(name, entity.getName());
            assertEquals(legalName, entity.getLegalName());
            assertEquals(cnpj, entity.getCnpj());
            assertEquals(email, entity.getEmail());
            assertEquals(phone, entity.getPhone());
        }
    }

    @Nested
    @DisplayName("Business Scenarios")
    class BusinessScenariosTests {
        @Test
        @DisplayName("should create active supplier with full info")
        void shouldCreateActiveSupplierWithFullInfo() {
            final SupplierJpaEntity entity = SupplierJpaEntity.builder()
                .name("Complete Supplier")
                .legalName("Complete Supplier LTDA")
                .cnpj("55667788000111")
                .email("complete@supplier.com")
                .phone("41966554433")
                .isActive(true)
                .build();

            assertTrue(entity.isActive());
            assertEquals("Complete Supplier", entity.getName());
            assertEquals("Complete Supplier LTDA", entity.getLegalName());
            assertEquals("55667788000111", entity.getCnpj());
            assertEquals("complete@supplier.com", entity.getEmail());
            assertEquals("41966554433", entity.getPhone());
        }

        @Test
        @DisplayName("should create deactivated supplier")
        void shouldCreateDeactivatedSupplier() {
            final Instant deactivatedAt = Instant.now();

            final SupplierJpaEntity entity = SupplierJpaEntity.builder()
                .name("Old Supplier")
                .isActive(false)
                .deactivatedAt(deactivatedAt)
                .build();

            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals("Old Supplier", entity.getName());
        }
    }
}
