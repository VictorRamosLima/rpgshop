package rpgshop.infraestructure.mapper.customer;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;

public final class CreditCardMapper {
    private CreditCardMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static CreditCard toDomain(final CreditCardJpaEntity entity) {
        Assert.notNull(entity, "'CreditCardJpaEntity' should not be null");

        return CreditCard.builder()
            .id(entity.getId())
            .cardNumber(entity.getCardNumber())
            .printedName(entity.getPrintedName())
            .cardBrand(CardBrandMapper.toDomain(entity.getCardBrand()))
            .securityCode(entity.getSecurityCode())
            .isPreferred(entity.isPreferred())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CreditCardJpaEntity toEntity(final CreditCard domain) {
        Assert.notNull(domain, "'CreditCard' should not be null");

        return CreditCardJpaEntity.builder()
            .id(domain.id())
            .cardNumber(domain.cardNumber())
            .printedName(domain.printedName())
            .cardBrand(CardBrandMapper.toEntity(domain.cardBrand()))
            .securityCode(domain.securityCode())
            .isPreferred(domain.isPreferred())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
