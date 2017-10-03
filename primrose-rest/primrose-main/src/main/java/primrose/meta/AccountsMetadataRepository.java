package primrose.meta;

import static pimrose.jooq.Primrose.PRIMROSE;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsMetadataRepository {
  private final DSLContext create;

  public AccountsMetadataRepository(final DSLContext create) {
    this.create = create;
  }

  public List<String> loadTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .fetch(0, String.class);
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
