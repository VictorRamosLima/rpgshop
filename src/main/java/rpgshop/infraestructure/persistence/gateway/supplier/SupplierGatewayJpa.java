package rpgshop.infraestructure.persistence.gateway.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.domain.entity.supplier.Supplier;
import rpgshop.infraestructure.persistence.mapper.supplier.SupplierMapper;
import rpgshop.infraestructure.persistence.repository.supplier.SupplierRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class SupplierGatewayJpa implements SupplierGateway {
    private final SupplierRepository supplierRepository;

    public SupplierGatewayJpa(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier save(final Supplier supplier) {
        final var entity = SupplierMapper.toEntity(supplier);
        final var saved = supplierRepository.save(entity);
        return SupplierMapper.toDomain(saved);
    }

    @Override
    public Optional<Supplier> findById(final UUID id) {
        return supplierRepository.findById(id).map(SupplierMapper::toDomain);
    }

    @Override
    public Page<Supplier> findActiveSuppliers(final Pageable pageable) {
        return supplierRepository.findByIsActiveTrue(pageable)
            .map(SupplierMapper::toDomain);
    }

    @Override
    public boolean existsByCnpj(final String cnpj) {
        return supplierRepository.existsByCnpj(cnpj);
    }
}
