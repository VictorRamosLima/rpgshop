package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
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

    boolean existsByIdAndCustomerId(
        @Nonnull final UUID addressId,
        @Nonnull final UUID customerId
    );

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM AddressJpaEntity a
        LEFT JOIN a.customer c
        WHERE a.id = :addressId
            AND (
                c.id = :customerId
                OR c IS NULL
            )
        """)
    boolean existsByIdAndCustomerIdOrWithoutCustomer(
        @Param("addressId") @Nonnull final UUID addressId,
        @Param("customerId") @Nonnull final UUID customerId
    );
}
