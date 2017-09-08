package primrose.addresses;

import java.util.List;
import java.util.stream.Collector;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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
          .where(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE_CODE.eq(DSL.value(addressType)))
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
          .where(PRIMROSE.T_ACCOUNT_ADDRESS_TYPES.ACCOUNT_ADDRESS_TYPE_CODE.eq(DSL.value(addressType)))
          .asField())
      .execute();
  }

  public void insert(final long addressId, final Address address) {
    create.insertInto(PRIMROSE.T_ADDRESSES)
      .columns(
        PRIMROSE.T_ADDRESSES.ADDRESS_ID,
        PRIMROSE.T_ADDRESSES.STREET,
        PRIMROSE.T_ADDRESSES.STATE,
        PRIMROSE.T_ADDRESSES.POSTAL_CODE,
        PRIMROSE.T_ADDRESSES.COUNTRY,
        PRIMROSE.T_ADDRESSES.CITY)
      .values(
        DSL.value(addressId),
        DSL.value(address.street()),
        DSL.value(address.state()),
        DSL.value(address.postalCode()),
        DSL.value(address.country()),
        DSL.value(address.city()))
      .execute();
  }

  public Address get(final long addressId) {
    return create
      .select(
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

  public Multimap<AddressType, Address> getByAccountId(final long accountId) {
    return create
      .select(
        ADDRESS.ADDRESS_CODE,
        ADDRESS.STREET,
        ADDRESS.STATE,
        ADDRESS.POSTAL_CODE,
        ADDRESS.COUNTRY,
        ADDRESS.CITY,
        ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)
      .from(ADDRESS)
      .leftJoin(ACCOUNT_ADDRESS).on(ACCOUNT_ADDRESS.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
      .leftJoin(ACCOUNT_ADDRESS_TYPE)
      .on(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ADDRESS_TYPE_ID))
      .where(ACCOUNT_ADDRESS.ACCOUNT_ID.eq(DSL.value(accountId)))
      .fetch()
      .stream()
      .collect(Collector.of(
        HashMultimap::create,
        (map, row) -> map.put(AddressType.of(row.get(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)), map(row)),
        (map1, map2) -> {
          map1.putAll(map2);
          return map1;
        }));
  }

  public Multimap<AddressType, Address> getByAccountCode(final String accountCode) {
    return create
      .select(
        ADDRESS.ADDRESS_CODE,
        ADDRESS.STREET,
        ADDRESS.STATE,
        ADDRESS.POSTAL_CODE,
        ADDRESS.COUNTRY,
        ADDRESS.CITY,
        ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)
      .from(ADDRESS)
      .leftJoin(ACCOUNT_ADDRESS).on(ACCOUNT_ADDRESS.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
      .leftJoin(ACCOUNT).on(ACCOUNT.ACCOUNT_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ID))
      .leftJoin(ACCOUNT_ADDRESS_TYPE)
      .on(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_ID.eq(ACCOUNT_ADDRESS.ACCOUNT_ADDRESS_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_CODE.eq(DSL.value(accountCode)))
      .fetch()
      .stream()
      .collect(Collector.of(
        HashMultimap::create,
        (map, row) -> map.put(AddressType.of(row.get(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)), map(row)),
        (map1, map2) -> {
          map1.putAll(map2);
          return map1;
        }));
  }

  public List<AddressType> getTypes() {
    return create
      .select(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)
      .from(ACCOUNT_ADDRESS_TYPE)
      .fetch(record -> AddressType.of(record.get(ACCOUNT_ADDRESS_TYPE.ACCOUNT_ADDRESS_TYPE_CODE)));
  }

  private Address map(final Record record) {
    return ImmutableAddress.builder()
      .code(record.getValue(PRIMROSE.T_ADDRESSES.ADDRESS_CODE))
      .street(record.getValue(PRIMROSE.T_ADDRESSES.STREET))
      .state(record.getValue(PRIMROSE.T_ADDRESSES.STATE))
      .postalCode(record.getValue(PRIMROSE.T_ADDRESSES.POSTAL_CODE))
      .country(record.getValue(PRIMROSE.T_ADDRESSES.COUNTRY))
      .city(record.getValue(PRIMROSE.T_ADDRESSES.CITY))
      .build();
  }
}
