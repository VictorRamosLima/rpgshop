package rpgshop.application.gateway.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.supplier.Supplier;

import java.util.Optional;
import java.util.UUID;

public interface SupplierGateway {
    Supplier save(final Supplier supplier);
    Optional<Supplier> findById(final UUID id);
    Page<Supplier> findActiveSuppliers(final Pageable pageable);
    boolean existsByCnpj(final String cnpj);
}
