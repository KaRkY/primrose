package primrose.accounts;

import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.value;
import static org.jooq.impl.DSL.when;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.ACCOUNTS_SEQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.util.IdUtil;
import primrose.util.QueryUtil;

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
          .where(PRIMROSE.ACCOUNT_TYPES.NAME.eq(account.type()))
          .asField(),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  public Optional<Account> loadById(final long accountId) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.ID,
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
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  public Optional<Account> loadByName(final String accountName) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.ID,
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
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  public List<Account> loadBySearch(final AccountsSearch accountSearch) {
    final List<Condition> conditions = new ArrayList<>();

    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().type(), PRIMROSE.ACCOUNT_TYPES.NAME);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().displayName(), PRIMROSE.ACCOUNTS.DISPLAY_NAME);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().name(), PRIMROSE.ACCOUNTS.NAME);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().email(), PRIMROSE.ACCOUNTS.EMAIL);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().phone(), PRIMROSE.ACCOUNTS.PHONE);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().website(), PRIMROSE.ACCOUNTS.WEBSITE);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.account().description(), PRIMROSE.ACCOUNTS.DESCRIPTION);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().street(), PRIMROSE.ADDRESSES.STREET);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().streetNumber(), PRIMROSE.ADDRESSES.STREET_NUMBER);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().city(), PRIMROSE.ADDRESSES.CITY);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().postalCode(), PRIMROSE.ADDRESSES.POSTAL_CODE);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().state(), PRIMROSE.ADDRESSES.STATE);
    QueryUtil.addLikeIgnoreCase(conditions, accountSearch.address().country(), PRIMROSE.ADDRESSES.COUNTRY);

    final int offset = accountSearch.page() != null && accountSearch.size() != null
      ? (accountSearch.page() - 1) * accountSearch.size()
      : 0;

    final int limit = accountSearch.size() != null
      ? accountSearch.size()
      : Integer.MAX_VALUE;

    return create
      .selectDistinct(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .leftJoin(PRIMROSE.ACCOUNT_ADDRESSES).on(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT.eq(PRIMROSE.ACCOUNTS.ID))
      .leftJoin(PRIMROSE.ADDRESSES).on(PRIMROSE.ADDRESSES.ID.eq(PRIMROSE.ACCOUNT_ADDRESSES.ADDRESS))
      .where(conditions)
      .orderBy(QueryUtil.map(accountSearch.sort(), field -> {
        switch (field) {
          case "type":
            return PRIMROSE.ACCOUNT_TYPES.NAME;
          case "displayName":
            return PRIMROSE.ACCOUNTS.DISPLAY_NAME;
          case "name":
            return PRIMROSE.ACCOUNTS.NAME;
          case "email":
            return PRIMROSE.ACCOUNTS.EMAIL;
          case "phone":
            return PRIMROSE.ACCOUNTS.PHONE;
          case "website":
            return PRIMROSE.ACCOUNTS.WEBSITE;
          case "description":
            return PRIMROSE.ACCOUNTS.DESCRIPTION;
          default:
            return null;
        }
      }))
      .offset(offset)
      .limit(limit)
      .fetch(record -> ImmutableAccount
        .builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  public boolean typeExists(final String type) {
    return create
      .select(
        when(exists(create
          .selectOne()
          .from(PRIMROSE.ACCOUNT_TYPES)
          .where(PRIMROSE.ACCOUNT_TYPES.NAME.eq(type))),
          true)
            .otherwise(false))
      .fetchOne()
      .value1();

  }
}
