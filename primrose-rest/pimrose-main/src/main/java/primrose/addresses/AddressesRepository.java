package primrose.addresses;

import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import pimrose.jooq.DefaultCatalog;
import pimrose.jooq.Primrose;
import pimrose.jooq.Sequences;
import pimrose.jooq.tables.TAccountAddressTypes;
import pimrose.jooq.tables.TAccountAddresses;
import pimrose.jooq.tables.TAccounts;
import pimrose.jooq.tables.TAddresses;

@Repository
public class AddressesRepository {
  private static final Primrose PRIMROSE = DefaultCatalog.DEFAULT_CATALOG.PRIMROSE;
  private static final TAddresses ADDRESS = PRIMROSE.T_ADDRESSES.as("address");
  private static final TAccounts ACCOUNT = PRIMROSE.T_ACCOUNTS.as("account");
  private static final TAccountAddresses ACCOUNT_ADDRESS = PRIMROSE.T_ACCOUNT_ADDRESSES.as("account_address");
  private static final TAccountAddressTypes ACCOUNT_ADDRESS_TYPE = PRIMROSE.T_ACCOUNT_ADDRESS_TYPES
    .as("account_address_type");
  private final DSLContext create;

  public AddressesRepository(final DSLContext create) {
    this.create = create;
  }

  public long getNewId() {
    return create.select(Sequences.S_ADDRESS.nextval()).fetchOne().value1();
  }

  public void insert(final String addressType, final long addressId, final long accountId) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_ADDRESSES)
      .columns(
        PRIMROSE.T_ACCOUNT_ADDRESSES.ACCOUNT_ID,
        PRIMROSE.T_ACCOUNT_ADDRESSES.ADDRESS_ID,
        PRIMROSE.T_ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE_ID)
      .values(
        DSL.value(accountId),
        DSL.value(addressId),
        create.select(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES)
          .where(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE.eq(DSL.value(addressType)))
          .asField())
      .execute();
  }

  public void insert(final String addressType, final long addressId, final String accountCode) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_ADDRESSES)
      .columns(
        PRIMROSE.T_ACCOUNT_ADDRESSES.ACCOUNT_ID,
        PRIMROSE.T_ACCOUNT_ADDRESSES.ADDRESS_ID,
        PRIMROSE.T_ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE_ID)
      .values(
        create.select(PRIMROSE.T_ACCOUNTS.ACCOUNT_ID)
          .from(PRIMROSE.T_ACCOUNTS)
          .where(PRIMROSE.T_ACCOUNTS.ACCOUNT_CODE.eq(DSL.value(accountCode)))
          .asField(),
        DSL.value(addressId),
        create.select(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES)
          .where(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE.eq(DSL.value(addressType)))
          .asField())
      .execute();
  }

  public void insert(final Address address) {
    create.insertInto(PRIMROSE.T_ADDRESSES)
      .columns(
        PRIMROSE.T_ADDRESSES.ADDRESS_ID,
        PRIMROSE.T_ADDRESSES.STREET,
        PRIMROSE.T_ADDRESSES.STATE,
        PRIMROSE.T_ADDRESSES.POSTAL_CODE,
        PRIMROSE.T_ADDRESSES.COUNTRY,
        PRIMROSE.T_ADDRESSES.CITY)
      .values(
        DSL.value(address.getId()),
        DSL.value(address.getStreet()),
        DSL.value(address.getState()),
        DSL.value(address.getPostalCode()),
        DSL.value(address.getCountry()),
        DSL.value(address.getCity()))
      .execute();
  }

  public Address get(final long addressId) {
    return create
      .select(
        ADDRESS.ADDRESS_ID,
        ADDRESS.ADDRESS_CODE,
        ADDRESS.STREET,
        ADDRESS.STATE,
        ADDRESS.POSTAL_CODE,
        ADDRESS.COUNTRY,
        ADDRESS.CITY)
      .from(ADDRESS)
      .where(ADDRESS.ADDRESS_ID.eq(DSL.value(addressId)))
      .fetchOptional(this::map)
      .orElseThrow(() -> new NoDataFoundException("Cursor did not return any result"));
  }

  public Map<String, List<Address>> getByAccountId(final long accountId) {
    return create
      .select(
        ADDRESS.ADDRESS_ID,
        ADDRESS.ADDRESS_CODE,
        ADDRESS.STREET,
        ADDRESS.STATE,
        ADDRESS.POSTAL_CODE,
        ADDRESS.COUNTRY,
        ADDRESS.CITY,
        ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE)
      .from(ADDRESS)
      .leftJoin(ACCOUNT_ADDRESS).on(ACCOUNT_ADDRESS.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
      .leftJoin(ACCOUNT_ADDRESS_TYPE)
      .on(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ADDRESS_TYPE_ID))
      .where(ACCOUNT_ADDRESS.ACCOUNT_ID.eq(DSL.value(accountId)))
      .fetchGroups(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE, this::map);
  }

  public Map<String, List<Address>> getByAccountCode(final String accountCode) {
    return create
      .select(
        ADDRESS.ADDRESS_ID,
        ADDRESS.ADDRESS_CODE,
        ADDRESS.STREET,
        ADDRESS.STATE,
        ADDRESS.POSTAL_CODE,
        ADDRESS.COUNTRY,
        ADDRESS.CITY,
        ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE)
      .from(ADDRESS)
      .leftJoin(ACCOUNT_ADDRESS).on(ACCOUNT_ADDRESS.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
      .leftJoin(ACCOUNT).on(ACCOUNT.ACCOUNT_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ID))
      .leftJoin(ACCOUNT_ADDRESS_TYPE)
      .on(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ADDRESS_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_CODE.eq(DSL.value(accountCode)))
      .fetchGroups(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE, this::map);
  }

  private Address map(final Record record) {
    final Address result = new Address();

    result.setId(record.getValue(PRIMROSE.T_ADDRESSES.ADDRESS_ID));
    result.setCode(record.getValue(PRIMROSE.T_ADDRESSES.ADDRESS_CODE));
    result.setStreet(record.getValue(PRIMROSE.T_ADDRESSES.STREET));
    result.setState(record.getValue(PRIMROSE.T_ADDRESSES.STATE));
    result.setPostalCode(record.getValue(PRIMROSE.T_ADDRESSES.POSTAL_CODE));
    result.setCountry(record.getValue(PRIMROSE.T_ADDRESSES.COUNTRY));
    result.setCity(record.getValue(PRIMROSE.T_ADDRESSES.CITY));

    return result;
  }
}
