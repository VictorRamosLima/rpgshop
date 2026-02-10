package rpgshop.infraestructure.persistence.mapper.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PhoneMapper")
class PhoneMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final PhoneType type = PhoneType.MOBILE;
            final String areaCode = "11";
            final String number = "999887766";
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final PhoneJpaEntity entity = PhoneJpaEntity.builder()
                .id(id)
                .type(type)
                .areaCode(areaCode)
                .number(number)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deactivatedAt(null)
                .build();

            final Phone domain = PhoneMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(type, domain.type());
            assertEquals(areaCode, domain.areaCode());
            assertEquals(number, domain.number());
            assertEquals(isActive, domain.isActive());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
            assertNull(domain.deactivatedAt());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> PhoneMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final PhoneType type = PhoneType.HOME;
            final String areaCode = "21";
            final String number = "33445566";
            final boolean isActive = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Instant deactivatedAt = Instant.now();

            final Phone domain = Phone.builder()
                .id(id)
                .type(type)
                .areaCode(areaCode)
                .number(number)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deactivatedAt(deactivatedAt)
                .build();

            final PhoneJpaEntity entity = PhoneMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(type, entity.getType());
            assertEquals(areaCode, entity.getAreaCode());
            assertEquals(number, entity.getNumber());
            assertEquals(isActive, entity.isActive());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> PhoneMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = PhoneMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
