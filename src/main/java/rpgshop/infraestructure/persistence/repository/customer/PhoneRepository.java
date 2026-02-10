package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = PhoneJpaEntity.class, idClass = UUID.class)
public interface PhoneRepository {
    @Nonnull
    PhoneJpaEntity save(@Nonnull final PhoneJpaEntity entity);

    @Nonnull
    Optional<PhoneJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<PhoneJpaEntity> findByCustomerId(
        @Nonnull final UUID customerId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    List<PhoneJpaEntity> findByCustomerIdAndIsActiveTrue(@Nonnull final UUID customerId);

    boolean existsByCustomerIdAndAreaCodeAndNumber(
        @Nonnull final UUID customerId,
        @Nonnull final String areaCode,
        @Nonnull final String number
    );
}
