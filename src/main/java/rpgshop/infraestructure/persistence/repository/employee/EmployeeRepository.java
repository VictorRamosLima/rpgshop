package rpgshop.infraestructure.persistence.repository.employee;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.employee.EmployeeJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = EmployeeJpaEntity.class, idClass = UUID.class)
public interface EmployeeRepository {
    @Nonnull
    EmployeeJpaEntity save(@Nonnull final EmployeeJpaEntity entity);

    Optional<EmployeeJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    @Query("""
        SELECT e FROM EmployeeJpaEntity e
        WHERE (:name IS NULL OR LOWER(CAST(e.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
            AND (:cpf IS NULL OR e.cpf = :cpf)
        """)
    Page<EmployeeJpaEntity> findByFilters(
        @Param("name") final String name,
        @Param("cpf") final String cpf,
        @Nonnull final Pageable pageable
    );

    boolean existsByCpf(@Nonnull final String cpf);
}
