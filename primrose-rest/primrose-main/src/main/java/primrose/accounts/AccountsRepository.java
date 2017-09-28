package primrose.accounts;

import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.ACCOUNTS_SEQ;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepository {

  private final DSLContext create;

  public AccountsRepository(final DSLContext create) {
    this.create = create;
  }

  public long nextValAccounts() {
    return create
      .select(ACCOUNTS_SEQ.nextval())
      .fetchOne()
      .value1();
  }

  public void insert(final long accountId, final Account account, final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNTS)
      .columns(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNTS.ACCOUNT_TYPE,
        PRIMROSE.ACCOUNTS.CREATED_BY)
      .values(
        value(accountId),
        value(account.name()),
        value(account.displayName()),
        value(account.description()),
        value(account.email()),
        value(account.phone()),
        create
          .select(PRIMROSE.ACCOUNT_TYPES.ID)
          .from(PRIMROSE.ACCOUNT_TYPES)
          .where(PRIMROSE.ACCOUNT_TYPES.NAME.eq(account.accountType()))
          .asField(),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  public Account loadById(final long accountId) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .where(PRIMROSE.ACCOUNTS.ID.eq(accountId))
      .fetchOptional(record -> ImmutableAccount
        .builder()
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .accountType(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build())
      .orElseThrow(() -> new NoDataFoundException("Missing data."));
  }

  public Account loadByName(final String accountName) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .where(PRIMROSE.ACCOUNTS.NAME.eq(accountName))
      .fetchOptional(record -> ImmutableAccount
        .builder()
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .accountType(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build())
      .orElseThrow(() -> new NoDataFoundException("Missing data."));
  }

  public List<String> loadTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .fetch(0, String.class);
  }
}
