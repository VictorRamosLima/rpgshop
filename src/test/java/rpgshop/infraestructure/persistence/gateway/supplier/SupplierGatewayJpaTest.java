package rpgshop.infraestructure.persistence.gateway.supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.supplier.Supplier;
import rpgshop.infraestructure.persistence.entity.supplier.SupplierJpaEntity;
import rpgshop.infraestructure.persistence.repository.supplier.SupplierRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierGatewayJpaTest {
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierGatewayJpa supplierGatewayJpa;

    @Test
    void shouldSaveSupplier() {
        final UUID supplierId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Supplier supplier = Supplier.builder()
            .id(supplierId)
            .name("RPG Supplier")
            .legalName("RPG Supplier LTDA")
            .cnpj("12345678000190")
            .email("supplier@example.com")
            .phone("11999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final SupplierJpaEntity savedEntity = SupplierJpaEntity.builder()
            .id(supplierId)
            .name("RPG Supplier")
            .legalName("RPG Supplier LTDA")
            .cnpj("12345678000190")
            .email("supplier@example.com")
            .phone("11999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(supplierRepository.save(any(SupplierJpaEntity.class))).thenReturn(savedEntity);

        final Supplier result = supplierGatewayJpa.save(supplier);

        assertNotNull(result);
        assertEquals(supplierId, result.id());
        assertEquals("RPG Supplier", result.name());
        verify(supplierRepository, times(1)).save(argThat(entity ->
            entity.getName().equals("RPG Supplier") && entity.getCnpj().equals("12345678000190")
        ));
    }

    @Test
    void shouldFindById() {
        final UUID supplierId = UUID.randomUUID();
        final Instant now = Instant.now();

        final SupplierJpaEntity entity = SupplierJpaEntity.builder()
            .id(supplierId)
            .name("RPG Supplier")
            .legalName("RPG Supplier LTDA")
            .cnpj("12345678000190")
            .email("supplier@example.com")
            .phone("11999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(entity));

        final Optional<Supplier> result = supplierGatewayJpa.findById(supplierId);

        assertTrue(result.isPresent());
        assertEquals(supplierId, result.get().id());
        assertEquals("RPG Supplier", result.get().name());
        verify(supplierRepository, times(1)).findById(supplierId);
    }

    @Test
    void shouldReturnEmptyWhenSupplierNotFoundById() {
        final UUID supplierId = UUID.randomUUID();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        final Optional<Supplier> result = supplierGatewayJpa.findById(supplierId);

        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findById(supplierId);
    }

    @Test
    void shouldFindActiveSuppliers() {
        final UUID supplierId1 = UUID.randomUUID();
        final UUID supplierId2 = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final SupplierJpaEntity entity1 = SupplierJpaEntity.builder()
            .id(supplierId1)
            .name("RPG Supplier 1")
            .legalName("RPG Supplier 1 LTDA")
            .cnpj("12345678000190")
            .email("supplier1@example.com")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final SupplierJpaEntity entity2 = SupplierJpaEntity.builder()
            .id(supplierId2)
            .name("RPG Supplier 2")
            .legalName("RPG Supplier 2 LTDA")
            .cnpj("98765432000190")
            .email("supplier2@example.com")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<SupplierJpaEntity> page = new PageImpl<>(List.of(entity1, entity2), pageable, 2);

        when(supplierRepository.findByIsActiveTrue(pageable)).thenReturn(page);

        final Page<Supplier> result = supplierGatewayJpa.findActiveSuppliers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("RPG Supplier 1", result.getContent().get(0).name());
        assertEquals("RPG Supplier 2", result.getContent().get(1).name());
        verify(supplierRepository, times(1)).findByIsActiveTrue(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoActiveSuppliers() {
        final Pageable pageable = PageRequest.of(0, 10);

        final Page<SupplierJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(supplierRepository.findByIsActiveTrue(pageable)).thenReturn(emptyPage);

        final Page<Supplier> result = supplierGatewayJpa.findActiveSuppliers(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findByIsActiveTrue(pageable);
    }

    @Test
    void shouldReturnTrueWhenExistsByCnpj() {
        final String cnpj = "12345678000190";

        when(supplierRepository.existsByCnpj(cnpj)).thenReturn(true);

        final boolean result = supplierGatewayJpa.existsByCnpj(cnpj);

        assertTrue(result);
        verify(supplierRepository, times(1)).existsByCnpj(cnpj);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByCnpj() {
        final String cnpj = "12345678000190";

        when(supplierRepository.existsByCnpj(cnpj)).thenReturn(false);

        final boolean result = supplierGatewayJpa.existsByCnpj(cnpj);

        assertFalse(result);
        verify(supplierRepository, times(1)).existsByCnpj(cnpj);
    }
}
