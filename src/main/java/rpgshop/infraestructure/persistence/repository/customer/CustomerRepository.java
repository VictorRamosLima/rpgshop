package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CustomerJpaEntity.class, idClass = UUID.class)
public interface CustomerRepository {
    @Nonnull
    CustomerJpaEntity save(@Nonnull final CustomerJpaEntity entity);

    Optional<CustomerJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    @Query("""
        SELECT c FROM CustomerJpaEntity c
        WHERE (:name IS NULL OR LOWER(CAST(c.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
            AND (:cpf IS NULL OR c.cpf = :cpf)
            AND (:email IS NULL OR LOWER(CAST(c.email AS string)) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%')))
            AND (:customerCode IS NULL OR c.customerCode = :customerCode)
            AND (:gender IS NULL OR c.gender = :gender)
            AND (:isActive IS NULL OR c.isActive = :isActive)
        """)
    Page<CustomerJpaEntity> findByFilters(
        @Param("name") @Nonnull final String name,
        @Param("cpf") @Nonnull final String cpf,
        @Param("email") @Nonnull final String email,
        @Param("customerCode") @Nonnull final String customerCode,
        @Param("gender") @Nonnull final Gender gender,
        @Param("isActive") @Nonnull final Boolean isActive,
        @Nonnull final Pageable pageable
    );

    boolean existsByCpf(@Nonnull final String cpf);

    boolean existsByEmail(@Nonnull final String email);
}
