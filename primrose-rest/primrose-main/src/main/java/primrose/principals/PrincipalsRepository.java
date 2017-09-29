package primrose.principals;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.field;
import static pimrose.jooq.Primrose.PRIMROSE;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class PrincipalsRepository {

  private final DSLContext create;

  public PrincipalsRepository(final DSLContext create) {
    this.create = create;
  }

  //@Cacheable("principals")
  public Principal loadPrincipal(final String username) {
    return create
      .select(
        PRIMROSE.PRINCIPALS.NAME,
        PRIMROSE.PRINCIPALS.ENABLED,
        PRIMROSE.PRINCIPALS.LOCKED,
        PRIMROSE.CREDENTIALS.VALUE)
      .from(PRIMROSE.PRINCIPALS)
      .join(PRIMROSE.CREDENTIALS)
      .on(PRIMROSE.CREDENTIALS.PRINCIPAL.eq(PRIMROSE.PRINCIPALS.ID))
      .and(currentLocalDateTime().between(PRIMROSE.CREDENTIALS.VALID_FROM, PRIMROSE.CREDENTIALS.VALID_TO))
      .where(PRIMROSE.PRINCIPALS.NAME.eq(username))
      .fetchOptional(record -> ImmutablePrincipal
        .builder()
        .name(record.getValue(PRIMROSE.PRINCIPALS.NAME))
        .enabled(record.getValue(PRIMROSE.PRINCIPALS.ENABLED))
        .locked(record.getValue(PRIMROSE.PRINCIPALS.LOCKED))
        .credidentials(record.getValue(PRIMROSE.CREDENTIALS.VALUE))
        .build())
      .orElseThrow(() -> new NoDataFoundException("Principal not found"))
      .withPermissions(loadPermissions(username));
  }

  @Cacheable("permissions")
  public List<String> loadPermissions(final String username) {
    return create
      .withRecursive("gr", "id", "name").as(create
        .select(PRIMROSE.PRINCIPAL_GROUPS.ID, PRIMROSE.PRINCIPAL_GROUPS.NAME)
        .from(PRIMROSE.PRINCIPAL_GROUPS)
        .join(PRIMROSE.PRINCIPAL_GROUP_MEMBERSHIPS)
        .on(PRIMROSE.PRINCIPAL_GROUP_MEMBERSHIPS.PRINCIPAL_GROUP.eq(PRIMROSE.PRINCIPAL_GROUPS.ID))
        .join(PRIMROSE.PRINCIPALS).on(PRIMROSE.PRINCIPALS.ID.eq(PRIMROSE.PRINCIPAL_GROUP_MEMBERSHIPS.PRINCIPAL))
        .where(PRIMROSE.PRINCIPALS.NAME.eq(username))
        .unionAll(
          create
            .select(PRIMROSE.PRINCIPAL_GROUPS.ID, PRIMROSE.PRINCIPAL_GROUPS.NAME)
            .from(PRIMROSE.PRINCIPAL_GROUPS)
            .join("gr").on(field("gr.id", Long.class).eq(PRIMROSE.PRINCIPAL_GROUPS.PARENT))))
      .select(concat(PRIMROSE.RESOURCES.NAME, ":").concat(PRIMROSE.OPERATIONS.NAME))
      .from("gr")
      .join(PRIMROSE.GROUP_ROLE_MEMBERSHIPS)
      .on(PRIMROSE.GROUP_ROLE_MEMBERSHIPS.PRINCIPAL_GROUP.eq(field("gr.id", Long.class)))
      .join(PRIMROSE.PRINCIPAL_ROLES).on(PRIMROSE.PRINCIPAL_ROLES.ID.eq(PRIMROSE.GROUP_ROLE_MEMBERSHIPS.PRINCIPAL_ROLE))
      .join(PRIMROSE.ROLE_PERMISSIONS).on(PRIMROSE.ROLE_PERMISSIONS.PRINCIPAL_ROLE.eq(PRIMROSE.PRINCIPAL_ROLES.ID))
      .join(PRIMROSE.OPERATIONS).on(PRIMROSE.OPERATIONS.ID.eq(PRIMROSE.ROLE_PERMISSIONS.OPERATION))
      .join(PRIMROSE.RESOURCES).on(PRIMROSE.RESOURCES.ID.eq(PRIMROSE.ROLE_PERMISSIONS.RESOURCE))
      .union(create
        .select(concat(PRIMROSE.RESOURCES.NAME, ":").concat(PRIMROSE.OPERATIONS.NAME))
        .from(PRIMROSE.PRINCIPALS)
        .join(PRIMROSE.PRINCIPAL_ROLE_MEMBERSHIPS)
        .on(PRIMROSE.PRINCIPAL_ROLE_MEMBERSHIPS.PRINCIPAL.eq(PRIMROSE.PRINCIPALS.ID))
        .join(PRIMROSE.PRINCIPAL_ROLES)
        .on(PRIMROSE.PRINCIPAL_ROLES.ID.eq(PRIMROSE.PRINCIPAL_ROLE_MEMBERSHIPS.PRINCIPAL_ROLE))
        .join(PRIMROSE.ROLE_PERMISSIONS).on(PRIMROSE.ROLE_PERMISSIONS.PRINCIPAL_ROLE.eq(PRIMROSE.PRINCIPAL_ROLES.ID))
        .join(PRIMROSE.OPERATIONS).on(PRIMROSE.OPERATIONS.ID.eq(PRIMROSE.ROLE_PERMISSIONS.OPERATION))
        .join(PRIMROSE.RESOURCES).on(PRIMROSE.RESOURCES.ID.eq(PRIMROSE.ROLE_PERMISSIONS.RESOURCE))
        .where(PRIMROSE.PRINCIPALS.NAME.eq(username)))
      .fetch(0, String.class);
  }

  @Cacheable("operations")
  public List<String> loadOperations() {
    return create
      .select(PRIMROSE.OPERATIONS.NAME)
      .from(PRIMROSE.OPERATIONS)
      .fetch(PRIMROSE.OPERATIONS.NAME);
  }

  @Cacheable("resources")
  public List<String> loadResources() {
    return create
      .select(PRIMROSE.RESOURCES.NAME)
      .from(PRIMROSE.RESOURCES)
      .fetch(PRIMROSE.RESOURCES.NAME);
  }
}
