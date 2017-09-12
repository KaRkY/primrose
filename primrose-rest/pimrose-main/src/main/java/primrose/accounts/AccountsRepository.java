package primrose.accounts;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import pimrose.jooq.DefaultCatalog;
import pimrose.jooq.Primrose;
import pimrose.jooq.Sequences;
import pimrose.jooq.tables.TAccountTypes;
import pimrose.jooq.tables.TAccounts;

@Repository
public class AccountsRepository {
  private static final Primrose PRIMROSE = DefaultCatalog.DEFAULT_CATALOG.PRIMROSE;
  private static final TAccounts ACCOUNT = PRIMROSE.T_ACCOUNTS.as("account");
  private static final TAccounts PARENT_ACCOUNT = PRIMROSE.T_ACCOUNTS.as("parent_account");
  private static final TAccountTypes ACCOUNT_TYPE = PRIMROSE.T_ACCOUNT_TYPES.as("account_type");

  private final DSLContext create;

  public AccountsRepository(final DSLContext create) {
    this.create = create;
  }

  public long getNewId() {
    return create.select(Sequences.S_ACCOUNT.nextval()).fetchOne().value1();
  }

  public void insert(final long accountId, final Account account) {
    create
      .insertInto(PRIMROSE.T_ACCOUNTS)
      .columns(
        PRIMROSE.T_ACCOUNTS.ACCOUNT_ID,
        PRIMROSE.T_ACCOUNTS.ACCOUNT_TYPE_ID,
        PRIMROSE.T_ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.T_ACCOUNTS.FULL_NAME,
        PRIMROSE.T_ACCOUNTS.EMAIL,
        PRIMROSE.T_ACCOUNTS.PHONE,
        PRIMROSE.T_ACCOUNTS.WEBSITE,
        PRIMROSE.T_ACCOUNTS.VALID_FROM,
        PRIMROSE.T_ACCOUNTS.VALID_TO)
      .values(
        DSL.value(accountId),
        create.select(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_TYPES)
          .where(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE_CODE.eq(DSL.value(account.type()))).asField(),
        DSL.value(account.displayName()),
        DSL.val(account.fullName()),
        DSL.value(account.email()),
        DSL.value(account.phone()),
        DSL.value(account.website()),
        account.validFrom() != null ? DSL.value(account.validTo()) : DSL.currentLocalDateTime(),
        DSL.value(account.validTo()))
      .execute();
  }

  public Account getById(final long accountId) {
    return create
      .select(
        ACCOUNT.ACCOUNT_CODE,
        ACCOUNT_TYPE.ACCOUNT_TYPE_CODE,
        PARENT_ACCOUNT.DISPLAY_NAME.as("parent_display_name"),
        ACCOUNT.PARENT_ACCOUNT_ID,
        ACCOUNT.DISPLAY_NAME,
        ACCOUNT.FULL_NAME,
        ACCOUNT.EMAIL,
        ACCOUNT.PHONE,
        ACCOUNT.WEBSITE,
        ACCOUNT.VALID_FROM,
        ACCOUNT.VALID_TO)
      .from(ACCOUNT)
      .leftJoin(PARENT_ACCOUNT).on(PARENT_ACCOUNT.ACCOUNT_ID.eq(ACCOUNT.PARENT_ACCOUNT_ID))
      .leftJoin(ACCOUNT_TYPE).on(ACCOUNT_TYPE.ACCOUNT_TYPE_ID.eq(ACCOUNT.ACCOUNT_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_ID.eq(DSL.value(accountId)))
      .fetchOptional(this::map)
      .orElseThrow(() -> new NoDataFoundException("Cursor did not return any result"));

  }

  public Account getByCode(final String code) {
    return create
      .select(
        ACCOUNT.ACCOUNT_CODE,
        ACCOUNT_TYPE.ACCOUNT_TYPE_CODE,
        PARENT_ACCOUNT.DISPLAY_NAME.as("parent_display_name"),
        ACCOUNT.PARENT_ACCOUNT_ID,
        ACCOUNT.DISPLAY_NAME,
        ACCOUNT.FULL_NAME,
        ACCOUNT.EMAIL,
        ACCOUNT.PHONE,
        ACCOUNT.WEBSITE,
        ACCOUNT.VALID_FROM,
        ACCOUNT.VALID_TO)
      .from(ACCOUNT)
      .leftJoin(PARENT_ACCOUNT).on(PARENT_ACCOUNT.ACCOUNT_ID.eq(ACCOUNT.PARENT_ACCOUNT_ID))
      .leftJoin(ACCOUNT_TYPE).on(ACCOUNT_TYPE.ACCOUNT_TYPE_ID.eq(ACCOUNT.ACCOUNT_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_CODE.eq(DSL.value(code)))
      .fetchOptional(this::map)
      .orElseThrow(() -> new NoDataFoundException("Cursor did not return any result"));
  }

  public List<AccountType> getTypes() {
    return create
      .select(ACCOUNT_TYPE.ACCOUNT_TYPE_CODE, ACCOUNT_TYPE.ACCOUNT_TYPE_DEFAULT)
      .from(ACCOUNT_TYPE)
      .fetch(record -> ImmutableAccountType
        .builder()
        .code(record.get(ACCOUNT_TYPE.ACCOUNT_TYPE_CODE))
        .def(record.get(ACCOUNT_TYPE.ACCOUNT_TYPE_DEFAULT))
        .build());
  }

  private Account map(final Record record) {

    return ImmutableAccount.builder()
      .code(record.getValue(PRIMROSE.T_ACCOUNTS.ACCOUNT_CODE))
      .type(record.getValue(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE_CODE))
      .displayName(record.getValue(PRIMROSE.T_ACCOUNTS.DISPLAY_NAME))
      .fullName(record.getValue(PRIMROSE.T_ACCOUNTS.FULL_NAME))
      .email(record.getValue(PRIMROSE.T_ACCOUNTS.EMAIL))
      .phone(record.getValue(PRIMROSE.T_ACCOUNTS.PHONE))
      .website(record.getValue(PRIMROSE.T_ACCOUNTS.WEBSITE))
      .validFrom(record.getValue(PRIMROSE.T_ACCOUNTS.VALID_FROM))
      .validTo(record.getValue(PRIMROSE.T_ACCOUNTS.VALID_TO))
      .build();
  }
}
