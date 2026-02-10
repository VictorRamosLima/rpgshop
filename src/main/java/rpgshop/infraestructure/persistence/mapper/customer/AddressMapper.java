package rpgshop.infraestructure.persistence.mapper.customer;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Address;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;

public final class AddressMapper {
    private AddressMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Address toDomain(final AddressJpaEntity entity) {
        Assert.notNull(entity, "'AddressJpaEntity' should not be null");

        return Address.builder()
            .id(entity.getId())
            .purpose(entity.getPurpose())
            .label(entity.getLabel())
            .residenceType(entity.getResidenceType())
            .streetType(entity.getStreetType())
            .street(entity.getStreet())
            .number(entity.getNumber())
            .neighborhood(entity.getNeighborhood())
            .zipCode(entity.getZipCode())
            .city(entity.getCity())
            .state(entity.getState())
            .country(entity.getCountry())
            .observations(entity.getObservations())
            .isActive(entity.isActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deactivatedAt(entity.getDeactivatedAt())
            .build();
    }

    @Nonnull
    public static AddressJpaEntity toEntity(final Address domain) {
        Assert.notNull(domain, "'Address' should not be null");

        return AddressJpaEntity.builder()
            .id(domain.id())
            .purpose(domain.purpose())
            .label(domain.label())
            .residenceType(domain.residenceType())
            .streetType(domain.streetType())
            .street(domain.street())
            .number(domain.number())
            .neighborhood(domain.neighborhood())
            .zipCode(domain.zipCode())
            .city(domain.city())
            .state(domain.state())
            .country(domain.country())
            .observations(domain.observations())
            .isActive(domain.isActive())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .deactivatedAt(domain.deactivatedAt())
            .build();
    }
}
