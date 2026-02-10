package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CreditCardJpaEntity.class, idClass = UUID.class)
public interface CreditCardRepository {
    @Nonnull
    CreditCardJpaEntity save(@Nonnull final CreditCardJpaEntity entity);

    @Nonnull
    Optional<CreditCardJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    List<CreditCardJpaEntity> findByCustomerIdAndIsActiveTrue(@Nonnull final UUID customerId);

    @Nonnull
    Optional<CreditCardJpaEntity> findByCustomerIdAndIsPreferredTrue(@Nonnull final UUID customerId);

    @Modifying
    @Query("UPDATE CreditCardJpaEntity cc SET cc.isPreferred = false WHERE cc.customer.id = :customerId")
    int clearPreferredByCustomerId(@Param("customerId") @Nonnull final UUID customerId);

    boolean existsByCustomerIdAndCardNumber(
        @Nonnull final UUID customerId,
        @Nonnull final String cardNumber
    );

    boolean existsByIdAndCustomerIdAndIsActiveTrue(
        @Nonnull final UUID creditCardId,
        @Nonnull final UUID customerId
    );
}
