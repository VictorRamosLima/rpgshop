package rpgshop.infraestructure.persistence.mapper.log;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.infraestructure.persistence.entity.log.TransactionLogJpaEntity;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public final class TransactionLogMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TransactionLogMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static TransactionLog toDomain(final TransactionLogJpaEntity entity) {
        Assert.notNull(entity, "'TransactionLogJpaEntity' should not be null");

        return TransactionLog.builder()
            .id(entity.getId())
            .entityName(entity.getEntityName())
            .entityId(entity.getEntityId())
            .operation(entity.getOperation())
            .userId(entity.getUser() != null ? entity.getUser().getId() : null)
            .timestamp(entity.getTimestamp())
            .previousData(entity.getPreviousData() != null ? entity.getPreviousData().toString() : null)
            .newData(entity.getNewData() != null ? entity.getNewData().toString() : null)
            .build();
    }

    @Nonnull
    public static TransactionLogJpaEntity toEntity(final TransactionLog domain) {
        Assert.notNull(domain, "'TransactionLog' should not be null");

        final TransactionLogJpaEntity.TransactionLogJpaEntityBuilder builder = TransactionLogJpaEntity.builder()
            .id(domain.id())
            .entityName(domain.entityName())
            .entityId(domain.entityId())
            .operation(domain.operation())
            .timestamp(domain.timestamp())
            .previousData(parseJson(domain.previousData()))
            .newData(parseJson(domain.newData()));

        if (domain.userId() != null) {
            builder.user(UserJpaEntity.builder().id(domain.userId()).build());
        }

        return builder.build();
    }

    private static JsonNode parseJson(final String json) {
        if (json == null) return null;
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }
}
