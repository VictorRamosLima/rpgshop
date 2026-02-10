package rpgshop.infraestructure.mapper.customer;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.util.ArrayList;
import java.util.Collections;

public final class CustomerMapper {
    private CustomerMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Customer toDomain(final CustomerJpaEntity entity) {
        Assert.notNull(entity, "'CustomerJpaEntity' should not be null");

        var phones = entity.getPhones() != null
            ? entity.getPhones().stream().map(PhoneMapper::toDomain).toList()
            : Collections.<Phone>emptyList();

        var addresses = entity.getAddresses() != null
            ? entity.getAddresses().stream().map(AddressMapper::toDomain).toList()
            : Collections.<Address>emptyList();

        var creditCards = entity.getCreditCards() != null
            ? entity.getCreditCards().stream().map(CreditCardMapper::toDomain).toList()
            : Collections.<CreditCard>emptyList();

        return Customer.builder()
            .id(entity.getId())
            .gender(entity.getGender())
            .name(entity.getName())
            .dateOfBirth(entity.getDateOfBirth())
            .cpf(entity.getCpf())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .ranking(entity.getRanking())
            .customerCode(entity.getCustomerCode())
            .phones(phones)
            .addresses(addresses)
            .creditCards(creditCards)
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CustomerJpaEntity toEntity(final Customer domain) {
        Assert.notNull(domain, "'Customer' should not be null");

        var entity = CustomerJpaEntity.builder()
            .id(domain.id())
            .gender(domain.gender())
            .name(domain.name())
            .dateOfBirth(domain.dateOfBirth())
            .cpf(domain.cpf())
            .email(domain.email())
            .password(domain.password())
            .ranking(domain.ranking())
            .customerCode(domain.customerCode())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();

        if (domain.phones() != null) {
            var phones = new ArrayList<>(domain.phones().stream()
                .map(PhoneMapper::toEntity).toList());
            phones.forEach(phone -> phone.setCustomer(entity));
            entity.setPhones(phones);
        }

        if (domain.addresses() != null) {
            var addresses = new ArrayList<>(domain.addresses().stream()
                .map(AddressMapper::toEntity).toList());
            addresses.forEach(address -> address.setCustomer(entity));
            entity.setAddresses(addresses);
        }

        if (domain.creditCards() != null) {
            var creditCards = new ArrayList<>(domain.creditCards().stream()
                .map(CreditCardMapper::toEntity).toList());
            creditCards.forEach(card -> card.setCustomer(entity));
            entity.setCreditCards(creditCards);
        }

        return entity;
    }
}
