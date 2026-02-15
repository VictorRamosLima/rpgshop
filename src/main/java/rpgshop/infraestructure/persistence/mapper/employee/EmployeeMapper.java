package rpgshop.infraestructure.persistence.mapper.employee;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.employee.Employee;
import rpgshop.infraestructure.persistence.entity.employee.EmployeeJpaEntity;

public final class EmployeeMapper {
    private EmployeeMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Employee toDomain(final EmployeeJpaEntity entity) {
        Assert.notNull(entity, "'EmployeeJpaEntity' should not be null");

        return Employee.builder()
            .id(entity.getId())
            .name(entity.getName())
            .cpf(entity.getCpf())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static EmployeeJpaEntity toEntity(final Employee domain) {
        Assert.notNull(domain, "'Employee' should not be null");

        return EmployeeJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .cpf(domain.cpf())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
