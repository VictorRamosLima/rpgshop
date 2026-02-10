package rpgshop.infraestructure.persistence.mapper.supplier;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.supplier.Supplier;
import rpgshop.infraestructure.persistence.entity.supplier.SupplierJpaEntity;

public final class SupplierMapper {
    private SupplierMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Supplier toDomain(final SupplierJpaEntity entity) {
        Assert.notNull(entity, "'SupplierJpaEntity' should not be null");

        return Supplier.builder()
            .id(entity.getId())
            .name(entity.getName())
            .legalName(entity.getLegalName())
            .cnpj(entity.getCnpj())
            .email(entity.getEmail())
            .phone(entity.getPhone())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static SupplierJpaEntity toEntity(final Supplier domain) {
        Assert.notNull(domain, "'Supplier' should not be null");

        return SupplierJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .legalName(domain.legalName())
            .cnpj(domain.cnpj())
            .email(domain.email())
            .phone(domain.phone())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
