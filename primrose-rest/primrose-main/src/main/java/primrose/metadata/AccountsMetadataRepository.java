package primrose.metadata;

import static pimrose.jooq.Primrose.PRIMROSE;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.meta.ImmutableAccountType;

@Repository
public class AccountsMetadataRepository {

  private final DSLContext create;

  public AccountsMetadataRepository(final DSLContext create) {
    this.create = create;
  }

  public List<AccountType> loadTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .fetch(record -> ImmutableAccountType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  public Optional<AccountType> loadType(final String type) {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .where(PRIMROSE.ACCOUNT_TYPES.NAME.eq(type))
      .fetchOptional(record -> ImmutableAccountType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  public List<String> loadAddressTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
      .fetch(0, String.class);
  }

  public List<String> loadContactTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_CONTACT_TYPES)
      .fetch(0, String.class);
  }
}
