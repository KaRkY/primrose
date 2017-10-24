package primrose.data.jooq;

import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.value;
import static org.jooq.impl.DSL.when;
import static pimrose.data.jooq.Primrose.PRIMROSE;
import static pimrose.data.jooq.Sequences.ACCOUNTS_SEQ;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.data.AccountsRepository;
import primrose.model.input.BaseInputAccount;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountAddressType;
import primrose.model.output.BaseOutputAccountContactType;
import primrose.model.output.BaseOutputAccountType;
import primrose.model.sort.Sort;
import primrose.util.IdUtil;

@Repository
public class JooqAccountsRepository implements AccountsRepository {

  private final DSLContext create;

  public JooqAccountsRepository(final DSLContext create) {
    this.create = create;
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#assignAddress(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void assignAddress(
    final String accountId,
    final String addressId,
    final String addressType,
    final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNT_ADDRESSES)
      .columns(
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT,
        PRIMROSE.ACCOUNT_ADDRESSES.ADDRESS,
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE,
        PRIMROSE.ACCOUNT_ADDRESSES.CREATED_BY)
      .values(
        value(IdUtil.valueOfLongId(accountId)),
        value(IdUtil.valueOfLongId(addressId)),
        create
          .select(PRIMROSE.ACCOUNT_ADDRESS_TYPES.ID)
          .from(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
          .where(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME.eq(addressType))
          .asField(),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#assignContact(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void assignContact(
    final String accountId,
    final String contactId,
    final String contactType,
    final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNT_CONTACTS)
      .columns(
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT,
        PRIMROSE.ACCOUNT_CONTACTS.CONTACT,
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE,
        PRIMROSE.ACCOUNT_CONTACTS.CREATED_BY)
      .values(
        value(IdUtil.valueOfLongId(accountId)),
        value(IdUtil.valueOfLongId(contactId)),
        create
          .select(PRIMROSE.ACCOUNT_CONTACT_TYPES.ID)
          .from(PRIMROSE.ACCOUNT_CONTACT_TYPES)
          .where(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME.eq(contactType))
          .asField(),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#count()
   */
  @Override
  public int count() {
    return create
      .fetchCount(PRIMROSE.ACCOUNTS);
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#insert(java.lang.String,
   * primrose.model.input.BaseInputAccount, java.lang.String)
   */
  @Override
  public void insert(final String accountId, final BaseInputAccount account, final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNTS)
      .columns(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNTS.WEBSITE,
        PRIMROSE.ACCOUNTS.ACCOUNT_TYPE,
        PRIMROSE.ACCOUNTS.CREATED_BY)
      .values(
        value(IdUtil.valueOfLongId(accountId)),
        value(account.name()),
        value(account.displayName()),
        value(account.description()),
        value(account.email()),
        value(account.phone()),
        value(account.website()),
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

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#load(java.lang.Integer,
   * java.lang.Integer, primrose.model.sort.Sort)
   */
  @Override
  public List<BaseOutputAccount> load(final Integer page, final Integer size, final Sort sort) {
    final int offset = page != null && size != null
      ? (page - 1) * size
      : 0;

    final int limit = size != null && size >= 0
      ? size
      : Integer.MAX_VALUE;

    return create
      .selectDistinct(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME,
        PRIMROSE.ACCOUNTS.VALID_FROM,
        PRIMROSE.ACCOUNTS.VALID_TO)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .orderBy(QueryUtil.map(sort, field -> {
        switch (field) {
        case "id":
          return PRIMROSE.ACCOUNTS.ID;
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
      .fetch(record -> ImmutableOutputAccount.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .validFrom(record.getValue(PRIMROSE.ACCOUNTS.VALID_FROM))
        .validTo(record.getValue(PRIMROSE.ACCOUNTS.VALID_TO))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadAddressType(java.lang.String)
   */
  @Override
  public Optional<BaseOutputAccountAddressType> loadAddressType(final String type) {
    return create
      .select(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
      .where(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME.eq(type))
      .fetchOptional(record -> ImmutableOutputAccountAddressType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadAddressTypes()
   */
  @Override
  public List<BaseOutputAccountAddressType> loadAddressTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
      .fetch(record -> ImmutableOutputAccountAddressType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadById(java.lang.String)
   */
  @Override
  public Optional<BaseOutputAccount> loadById(final String accountId) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME,
        PRIMROSE.ACCOUNTS.VALID_FROM,
        PRIMROSE.ACCOUNTS.VALID_TO)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .where(PRIMROSE.ACCOUNTS.ID.eq(IdUtil.valueOfLongId(accountId)))
      .fetchOptional(record -> ImmutableOutputAccount.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .validFrom(record.getValue(PRIMROSE.ACCOUNTS.VALID_FROM))
        .validTo(record.getValue(PRIMROSE.ACCOUNTS.VALID_TO))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadByName(java.lang.String)
   */
  @Override
  public Optional<BaseOutputAccount> loadByName(final String accountName) {
    return create
      .select(
        PRIMROSE.ACCOUNTS.ID,
        PRIMROSE.ACCOUNTS.NAME,
        PRIMROSE.ACCOUNTS.DISPLAY_NAME,
        PRIMROSE.ACCOUNTS.DESCRIPTION,
        PRIMROSE.ACCOUNTS.EMAIL,
        PRIMROSE.ACCOUNTS.PHONE,
        PRIMROSE.ACCOUNT_TYPES.NAME,
        PRIMROSE.ACCOUNTS.VALID_FROM,
        PRIMROSE.ACCOUNTS.VALID_TO)
      .from(PRIMROSE.ACCOUNTS)
      .join(PRIMROSE.ACCOUNT_TYPES).on(PRIMROSE.ACCOUNT_TYPES.ID.eq(PRIMROSE.ACCOUNTS.ACCOUNT_TYPE))
      .where(PRIMROSE.ACCOUNTS.NAME.eq(accountName))
      .fetchOptional(record -> ImmutableOutputAccount.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ACCOUNTS.ID)))
        .name(record.getValue(PRIMROSE.ACCOUNTS.NAME))
        .displayName(record.getValue(PRIMROSE.ACCOUNTS.DISPLAY_NAME))
        .description(record.getValue(PRIMROSE.ACCOUNTS.DESCRIPTION))
        .email(record.getValue(PRIMROSE.ACCOUNTS.EMAIL))
        .phone(record.getValue(PRIMROSE.ACCOUNTS.PHONE))
        .type(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .validFrom(record.getValue(PRIMROSE.ACCOUNTS.VALID_FROM))
        .validTo(record.getValue(PRIMROSE.ACCOUNTS.VALID_TO))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadContactType(java.lang.String)
   */
  @Override
  public Optional<BaseOutputAccountContactType> loadContactType(final String type) {
    return create
      .select(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_CONTACT_TYPES)
      .where(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME.eq(type))
      .fetchOptional(record -> ImmutableOutputAccountContactType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadContactTypes()
   */
  @Override
  public List<BaseOutputAccountContactType> loadContactTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_CONTACT_TYPES)
      .fetch(record -> ImmutableOutputAccountContactType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadType(java.lang.String)
   */
  @Override
  public Optional<BaseOutputAccountType> loadType(final String type) {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .where(PRIMROSE.ACCOUNT_TYPES.NAME.eq(type))
      .fetchOptional(record -> ImmutableOutputAccountType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#loadTypes()
   */
  @Override
  public List<BaseOutputAccountType> loadTypes() {
    return create
      .select(PRIMROSE.ACCOUNT_TYPES.NAME)
      .from(PRIMROSE.ACCOUNT_TYPES)
      .fetch(record -> ImmutableOutputAccountType.builder()
        .name(record.getValue(PRIMROSE.ACCOUNT_TYPES.NAME))
        .build());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#nextValAccounts()
   */
  @Override
  public String nextValAccounts() {
    return IdUtil.toStringId(create
      .select(ACCOUNTS_SEQ.nextval())
      .fetchOne()
      .value1());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#typeExists(java.lang.String)
   */
  @Override
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

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.AccountsRepository#update(java.lang.String,
   * primrose.model.input.BaseInputAccount, java.lang.String)
   */
  @Override
  public void update(final String accountId, final BaseInputAccount account, final String user) {
    create
      .update(PRIMROSE.ACCOUNTS)
      .set(PRIMROSE.ACCOUNTS.NAME, account.name())
      .set(PRIMROSE.ACCOUNTS.DISPLAY_NAME, account.displayName())
      .set(PRIMROSE.ACCOUNTS.DESCRIPTION, account.description())
      .set(PRIMROSE.ACCOUNTS.EMAIL, account.email())
      .set(PRIMROSE.ACCOUNTS.PHONE, account.phone())
      .set(PRIMROSE.ACCOUNTS.EDITED_BY, create
        .select(PRIMROSE.PRINCIPALS.ID)
        .from(PRIMROSE.PRINCIPALS)
        .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
        .asField())
      .set(PRIMROSE.ACCOUNTS.EDITED_AT, currentLocalDateTime())
      .where(PRIMROSE.ACCOUNTS.ID.eq(IdUtil.valueOfLongId(accountId)))
      .execute();
  }
}