package primrose.accounts;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Table;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import pimrose.jooq.DefaultCatalog;
import pimrose.jooq.Primrose;
import pimrose.jooq.Sequences;
import pimrose.jooq.tables.TAccountSearch;
import pimrose.jooq.tables.TAccountTypes;
import pimrose.jooq.tables.TAccounts;
import primrose.repositories.SearchParameters;
import primrose.repositories.SearchResult;

@Repository
public class AccountsRepository {
  private static final Primrose       PRIMROSE       = DefaultCatalog.DEFAULT_CATALOG.PRIMROSE;
  private static final TAccounts      ACCOUNT        = PRIMROSE.T_ACCOUNTS.as("account");
  private static final TAccounts      PARENT_ACCOUNT = PRIMROSE.T_ACCOUNTS.as("parent_account");
  private static final TAccountTypes  ACCOUNT_TYPE   = PRIMROSE.T_ACCOUNT_TYPES.as("account_type");
  private static final TAccountSearch ACCOUNT_SEARCH = PRIMROSE.T_ACCOUNT_SEARCH.as("account_search");

  private final DSLContext create;

  public AccountsRepository(final DSLContext create) {
    this.create = create;
  }

  public long getNewCode() {
    return create.select(Sequences.S_ACCOUNT_CODE.nextval()).fetchOne().value1();
  }

  public SearchResult<Account> search(final SearchParameters searchParameters) {
    final List<Condition> conditions = new ArrayList<>();

    // Create condition for full text search
    if (searchParameters.getQuery() != null) {
      conditions.add(DSL
        .condition(
          "{0} @@ plainto_tsquery('pg_catalog.english', {1})",
          ACCOUNT_SEARCH.FULL_TEXT_SEARCH,
          searchParameters.getQuery()));
    }

    final Integer size = searchParameters.getSize();
    final Integer page = searchParameters.getPage();

    // Select account codes for 1 page filtered by condition
    final Table<Record2<String, Integer>> filtered = create
      .select(ACCOUNT.ACCOUNT_CODE, DSL.count().over().as("count"))
      .from(ACCOUNT)
      .leftJoin(ACCOUNT_SEARCH).on(ACCOUNT_SEARCH.ACCOUNT_CODE.eq(ACCOUNT.ACCOUNT_CODE))
      .where(conditions)
      .limit(size == null ? Integer.MAX_VALUE : size)
      .offset(page == null || size == null ? 0 : page * size - size)
      .asTable("filtered_account");

    // Select actual data
    return SearchResult.of(create
      .select(
        filtered.field(1, Integer.class),
        ACCOUNT.ACCOUNT_CODE,
        ACCOUNT.ACCOUNT_URL,
        ACCOUNT_TYPE.ACCOUNT_TYPE,
        PARENT_ACCOUNT.DISPLAY_NAME.as("parent_display_name"),
        ACCOUNT.PARENT_ACCOUNT_CODE,
        ACCOUNT.DISPLAY_NAME,
        ACCOUNT.FULL_NAME,
        ACCOUNT.EMAIL,
        ACCOUNT.PHONE,
        ACCOUNT.WEBSITE,
        ACCOUNT.VALID_FROM,
        ACCOUNT.VALID_TO)
      .from(filtered)
      .join(ACCOUNT).on(ACCOUNT.ACCOUNT_CODE.eq(filtered.field(0, String.class)))
      .leftJoin(PARENT_ACCOUNT).on(PARENT_ACCOUNT.ACCOUNT_CODE.eq(ACCOUNT.PARENT_ACCOUNT_CODE))
      .join(ACCOUNT_TYPE).on(ACCOUNT_TYPE.ACCOUNT_TYPE_ID.eq(ACCOUNT.ACCOUNT_TYPE_ID))
      .fetchGroups(filtered.field(1, Integer.class), this::map));
  }

  public void insert(final Account account) {
    create
      .insertInto(PRIMROSE.T_ACCOUNTS)
      .columns(
        PRIMROSE.T_ACCOUNTS.ACCOUNT_CODE,
        PRIMROSE.T_ACCOUNTS.PARENT_ACCOUNT_CODE,
        PRIMROSE.T_ACCOUNTS.ACCOUNT_TYPE_ID,
        PRIMROSE.T_ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.T_ACCOUNTS.FULL_NAME,
        PRIMROSE.T_ACCOUNTS.EMAIL,
        PRIMROSE.T_ACCOUNTS.PHONE,
        PRIMROSE.T_ACCOUNTS.WEBSITE,
        PRIMROSE.T_ACCOUNTS.VALID_FROM,
        PRIMROSE.T_ACCOUNTS.VALID_TO)
      .values(
        DSL.value(account.getCode()),
        DSL.value(account.getParentCode()),
        create.select(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_TYPES)
          .where(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE.eq(DSL.value(account.getType()))).asField(),
        DSL.value(account.getDisplayName()),
        DSL.val(account.getFullName()),
        DSL.value(account.getEmail()),
        DSL.value(account.getPhone()),
        DSL.value(account.getWebsite()),
        account.getValidFrom() != null ? DSL.value(account.getValidFrom()) : DSL.currentLocalDateTime(),
        DSL.value(account.getValidTo()))
      .execute();
  }

  public Account getByCode(final String code) {
    return create
      .select(
        ACCOUNT.ACCOUNT_CODE,
        ACCOUNT.ACCOUNT_URL,
        ACCOUNT_TYPE.ACCOUNT_TYPE,
        PARENT_ACCOUNT.DISPLAY_NAME.as("parent_display_name"),
        ACCOUNT.PARENT_ACCOUNT_CODE,
        ACCOUNT.DISPLAY_NAME,
        ACCOUNT.FULL_NAME,
        ACCOUNT.EMAIL,
        ACCOUNT.PHONE,
        ACCOUNT.WEBSITE,
        ACCOUNT.VALID_FROM,
        ACCOUNT.VALID_TO)
      .from(ACCOUNT)
      .leftJoin(PARENT_ACCOUNT).on(PARENT_ACCOUNT.ACCOUNT_CODE.eq(ACCOUNT.PARENT_ACCOUNT_CODE))
      .leftJoin(ACCOUNT_TYPE).on(ACCOUNT_TYPE.ACCOUNT_TYPE_ID.eq(ACCOUNT.ACCOUNT_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_CODE.eq(DSL.value(code)))
      .fetchOptional(this::map)
      .orElseThrow(() -> new NoDataFoundException("Cursor did not return any result"));

  }

  public Account getByUrl(final String url) {
    return create
      .select(
        ACCOUNT.ACCOUNT_CODE,
        ACCOUNT.ACCOUNT_URL,
        ACCOUNT_TYPE.ACCOUNT_TYPE,
        PARENT_ACCOUNT.DISPLAY_NAME.as("parent_display_name"),
        ACCOUNT.PARENT_ACCOUNT_CODE,
        ACCOUNT.DISPLAY_NAME,
        ACCOUNT.FULL_NAME,
        ACCOUNT.EMAIL,
        ACCOUNT.PHONE,
        ACCOUNT.WEBSITE,
        ACCOUNT.VALID_FROM,
        ACCOUNT.VALID_TO)
      .from(ACCOUNT)
      .leftJoin(PARENT_ACCOUNT).on(PARENT_ACCOUNT.ACCOUNT_CODE.eq(ACCOUNT.PARENT_ACCOUNT_CODE))
      .leftJoin(ACCOUNT_TYPE).on(ACCOUNT_TYPE.ACCOUNT_TYPE_ID.eq(ACCOUNT.ACCOUNT_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_URL.eq(DSL.value(url)))
      .fetchOptional(this::map)
      .orElseThrow(() -> new NoDataFoundException("Cursor did not return any result"));
  }

  private Account map(final Record record) {
    final Account acc = new Account();

    acc.setCode(record.getValue(PRIMROSE.T_ACCOUNTS.ACCOUNT_CODE));
    acc.setUrlCode(record.getValue(PRIMROSE.T_ACCOUNTS.ACCOUNT_URL));
    acc.setParentName(record.getValue(PRIMROSE.T_ACCOUNTS.DISPLAY_NAME.as("parent_display_name")));
    acc.setParentCode(record.getValue(PRIMROSE.T_ACCOUNTS.PARENT_ACCOUNT_CODE));
    acc.setType(record.getValue(PRIMROSE.T_ACCOUNT_TYPES.ACCOUNT_TYPE));
    acc.setDisplayName(record.getValue(PRIMROSE.T_ACCOUNTS.DISPLAY_NAME));
    acc.setFullName(record.getValue(PRIMROSE.T_ACCOUNTS.FULL_NAME));
    acc.setEmail(record.getValue(PRIMROSE.T_ACCOUNTS.EMAIL));
    acc.setPhone(record.getValue(PRIMROSE.T_ACCOUNTS.PHONE));
    acc.setWebsite(record.getValue(PRIMROSE.T_ACCOUNTS.WEBSITE));
    acc.setValidFrom(record.getValue(PRIMROSE.T_ACCOUNTS.VALID_FROM));
    acc.setValidTo(record.getValue(PRIMROSE.T_ACCOUNTS.VALID_TO));

    return acc;
  }
}
