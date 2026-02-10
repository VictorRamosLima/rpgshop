package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = AddressJpaEntity.class, idClass = UUID.class)
public interface AddressRepository {
    @Nonnull
    AddressJpaEntity save(@Nonnull final AddressJpaEntity entity);

    @Nonnull
    Optional<AddressJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<AddressJpaEntity> findByCustomerId(
        @Nonnull final UUID customerId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    List<AddressJpaEntity> findByCustomerIdAndIsActiveTrue(@Nonnull final UUID customerId);

    @Nonnull
    List<AddressJpaEntity> findByCustomerIdAndPurposeAndIsActiveTrue(
        @Nonnull final UUID customerId,
        @Nonnull final AddressPurpose purpose
    );

    boolean existsByCustomerIdAndPurposeAndIsActiveTrue(
        @Nonnull final UUID customerId,
        @Nonnull final AddressPurpose purpose
    );

    @Nonnull
    Optional<AddressJpaEntity> findByCustomerIdAndLabelIgnoreCase(
        @Nonnull final UUID customerId,
        @Nonnull final String label
    );
}
