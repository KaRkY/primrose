package primrose.addresses;

import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.ADDRESSES_SEQ;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.util.IdUtil;

@Repository
public class AddressesRepository {
  private final DSLContext create;

  public AddressesRepository(final DSLContext create) {
    this.create = create;
  }

  public long nextValAddresses() {
    return create
      .select(ADDRESSES_SEQ.nextval())
      .fetchOne()
      .value1();
  }

  public void insert(final long addressId, final Address address, final String user) {
    create
      .insertInto(PRIMROSE.ADDRESSES)
      .columns(
        PRIMROSE.ADDRESSES.ID,
        PRIMROSE.ADDRESSES.STREET,
        PRIMROSE.ADDRESSES.STREET_NUMBER,
        PRIMROSE.ADDRESSES.CITY,
        PRIMROSE.ADDRESSES.POSTAL_CODE,
        PRIMROSE.ADDRESSES.STATE,
        PRIMROSE.ADDRESSES.COUNTRY,
        PRIMROSE.ADDRESSES.CREATED_BY)
      .values(
        value(addressId),
        value(address.street()),
        value(address.streetNumber()),
        value(address.city()),
        value(address.postalCode()),
        value(address.state()),
        value(address.country()),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  public void insert(final long accountId, final long addressId, final String addressType, final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNT_ADDRESSES)
      .columns(
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT,
        PRIMROSE.ACCOUNT_ADDRESSES.ADDRESS,
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE,
        PRIMROSE.ACCOUNT_ADDRESSES.CREATED_BY)
      .values(
        value(accountId),
        value(addressId),
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

  public Optional<Address> loadById(final long addressId) {
    return create
      .select(
        PRIMROSE.ADDRESSES.ID,
        PRIMROSE.ADDRESSES.STREET,
        PRIMROSE.ADDRESSES.STREET_NUMBER,
        PRIMROSE.ADDRESSES.CITY,
        PRIMROSE.ADDRESSES.POSTAL_CODE,
        PRIMROSE.ADDRESSES.STATE,
        PRIMROSE.ADDRESSES.COUNTRY)
      .from(PRIMROSE.ADDRESSES)
      .where(PRIMROSE.ADDRESSES.ID.eq(addressId))
      .fetchOptional(record -> ImmutableAddress
        .builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

  public Optional<Address> loadById(final long accountId, final long addressId) {
    return create
      .select(
        PRIMROSE.ADDRESSES.ID,
        PRIMROSE.ADDRESSES.STREET,
        PRIMROSE.ADDRESSES.STREET_NUMBER,
        PRIMROSE.ADDRESSES.CITY,
        PRIMROSE.ADDRESSES.POSTAL_CODE,
        PRIMROSE.ADDRESSES.STATE,
        PRIMROSE.ADDRESSES.COUNTRY)
      .from(PRIMROSE.ADDRESSES)
      .join(PRIMROSE.ACCOUNT_ADDRESSES).on(PRIMROSE.ACCOUNT_ADDRESSES.ADDRESS.eq(PRIMROSE.ADDRESSES.ID))
      .where(
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT.eq(accountId),
        PRIMROSE.ADDRESSES.ID.eq(addressId))
      .fetchOptional(record -> ImmutableAddress
        .builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

  public Map<String, List<Address>> loadByAccountId(final long accountId) {
    return create
      .select(
        PRIMROSE.ADDRESSES.ID,
        PRIMROSE.ADDRESSES.STREET,
        PRIMROSE.ADDRESSES.STREET_NUMBER,
        PRIMROSE.ADDRESSES.CITY,
        PRIMROSE.ADDRESSES.POSTAL_CODE,
        PRIMROSE.ADDRESSES.STATE,
        PRIMROSE.ADDRESSES.COUNTRY,
        PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME)
      .from(PRIMROSE.ADDRESSES)
      .join(PRIMROSE.ACCOUNT_ADDRESSES).on(PRIMROSE.ACCOUNT_ADDRESSES.ADDRESS.eq(PRIMROSE.ADDRESSES.ID))
      .join(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
      .on(PRIMROSE.ACCOUNT_ADDRESS_TYPES.ID.eq(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE))
      .where(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT.eq(accountId))
      .fetchGroups(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME, record -> ImmutableAddress
        .builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

}
