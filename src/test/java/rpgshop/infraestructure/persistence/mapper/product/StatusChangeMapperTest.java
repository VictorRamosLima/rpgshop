package rpgshop.infraestructure.persistence.mapper.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.StatusChange;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;
import rpgshop.infraestructure.persistence.entity.product.StatusChangeJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.DEFECTIVE;
import static rpgshop.domain.entity.product.constant.StatusChangeType.ACTIVATE;
import static rpgshop.domain.entity.product.constant.StatusChangeType.DEACTIVATE;

@DisplayName("StatusChangeMapper")
class StatusChangeMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String reason = "Product out of stock";
            final StatusChangeCategory category = StatusChangeCategory.OUT_OF_MARKET;
            final StatusChangeType type = DEACTIVATE;
            final Instant createdAt = Instant.now();

            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .id(id)
                .reason(reason)
                .category(category)
                .type(type)
                .createdAt(createdAt)
                .build();

            final StatusChange domain = StatusChangeMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(reason, domain.reason());
            assertEquals(category, domain.category());
            assertEquals(type, domain.type());
            assertEquals(createdAt, domain.createdAt());
        }

        @Test
        @DisplayName("should map entity with ACTIVATE type")
        void shouldMapEntityWithActivateType() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .id(UUID.randomUUID())
                .reason("Stock replenished")
                .category(DEFECTIVE)
                .type(ACTIVATE)
                .createdAt(Instant.now())
                .build();

            final StatusChange domain = StatusChangeMapper.toDomain(entity);

            assertEquals(ACTIVATE, domain.type());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> StatusChangeMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String reason = "Manual deactivation";
            final StatusChangeCategory category = DEFECTIVE;
            final StatusChangeType type = DEACTIVATE;
            final Instant createdAt = Instant.now();

            final StatusChange domain = StatusChange.builder()
                .id(id)
                .reason(reason)
                .category(category)
                .type(type)
                .createdAt(createdAt)
                .build();

            final StatusChangeJpaEntity entity = StatusChangeMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(reason, entity.getReason());
            assertEquals(category, entity.getCategory());
            assertEquals(type, entity.getType());
            assertEquals(createdAt, entity.getCreatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> StatusChangeMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = StatusChangeMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
