package rpgshop.domain.entity.user;

import lombok.Builder;
import rpgshop.domain.entity.user.constant.UserType;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record User(
    UUID id,
    String email,
    String password,
    UserType userType,
    UUID userTypeId,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
