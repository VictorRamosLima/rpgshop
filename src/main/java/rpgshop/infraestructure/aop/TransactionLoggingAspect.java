package rpgshop.infraestructure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
public class TransactionLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(TransactionLoggingAspect.class);
    private final TransactionLogGateway transactionLogGateway;
    private final ObjectMapper objectMapper;

    public TransactionLoggingAspect(final TransactionLogGateway transactionLogGateway) {
        this.transactionLogGateway = transactionLogGateway;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Around("execution(* rpgshop.infraestructure.persistence.gateway..*.save(..)) " +
            "&& !target(rpgshop.infraestructure.persistence.gateway.log.TransactionLogGatewayJpa) " +
            "&& !target(rpgshop.infraestructure.persistence.gateway.user.UserGatewayJpa)")
    public Object logSaveOperation(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object argument = joinPoint.getArgs()[0];
        final UUID entityId = extractId(argument);
        final String entityName = resolveEntityName(argument);

        String previousData = null;
        OperationType operation = OperationType.INSERT;

        if (entityId != null) {
            final Object existingEntity = tryFindById(joinPoint.getTarget(), entityId);
            if (existingEntity != null) {
                operation = OperationType.UPDATE;
                previousData = serializeToJson(existingEntity);
            }
        }

        final Object result = joinPoint.proceed();

        try {
            final UUID resultId = extractId(result);
            final String newData = serializeToJson(result);
            final UUID userId = UserContextHolder.getCurrentUserId();

            final TransactionLog transactionLog = TransactionLog.builder()
                .id(UUID.randomUUID())
                .entityName(entityName)
                .entityId(resultId != null ? resultId : entityId)
                .operation(operation)
                .userId(userId)
                .timestamp(Instant.now())
                .previousData(previousData)
                .newData(newData)
                .build();

            transactionLogGateway.save(transactionLog);
        } catch (Exception e) {
            log.error("Failed to create transaction log for entity '{}': {}", entityName, e.getMessage(), e);
        }

        return result;
    }

    private UUID extractId(final Object entity) {
        if (entity == null) return null;
        try {
            final Method idMethod = entity.getClass().getMethod("id");
            return (UUID) idMethod.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private String resolveEntityName(final Object entity) {
        if (entity == null) return "Unknown";
        return entity.getClass().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    private Object tryFindById(final Object target, final UUID id) {
        try {
            final Method findByIdMethod = target.getClass().getMethod("findById", UUID.class);
            final Object result = findByIdMethod.invoke(target, id);
            if (result instanceof Optional<?> optional) {
                return optional.orElse(null);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String serializeToJson(final Object entity) {
        if (entity == null) return null;
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            log.warn("Failed to serialize entity to JSON: {}", e.getMessage());
            return entity.toString();
        }
    }
}
