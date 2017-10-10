package primrose.addresses;

import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.ADDRESSES_SEQ;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.pagging.sort.Sort;
import primrose.util.IdUtil;
import primrose.util.QueryUtil;

@Repository
public class AddressesRepository {

  private final DSLContext create;

  public AddressesRepository(final DSLContext create) {
    this.create = create;
  }

  public String nextValAddresses() {
    return IdUtil.toStringId(create
      .select(ADDRESSES_SEQ.nextval())
      .fetchOne()
      .value1());
  }

  public void insert(final Address address, final String user) {
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
        value(IdUtil.valueOfLongId(address.id())),
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

  public void update(final String addressId, final Address address, final String user) {
    create
      .update(PRIMROSE.ADDRESSES)
      .set(PRIMROSE.ADDRESSES.STREET, address.street())
      .set(PRIMROSE.ADDRESSES.STREET_NUMBER, address.streetNumber())
      .set(PRIMROSE.ADDRESSES.CITY, address.city())
      .set(PRIMROSE.ADDRESSES.POSTAL_CODE, address.postalCode())
      .set(PRIMROSE.ADDRESSES.STATE, address.state())
      .set(PRIMROSE.ADDRESSES.COUNTRY, address.country())
      .set(PRIMROSE.ADDRESSES.EDITED_BY, create
        .select(PRIMROSE.PRINCIPALS.ID)
        .from(PRIMROSE.PRINCIPALS)
        .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
        .asField())
      .set(PRIMROSE.ADDRESSES.EDITED_AT, currentLocalDateTime())
      .where(PRIMROSE.ADDRESSES.ID.eq(IdUtil.valueOfLongId(addressId)))
      .execute();
  }

  public Optional<Address> loadById(final String addressId) {
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
      .where(PRIMROSE.ADDRESSES.ID.eq(IdUtil.valueOfLongId(addressId)))
      .fetchOptional(record -> ImmutableAddress.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

  public Optional<Address> loadById(final String accountId, final String type, final String addressId) {
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
      .join(PRIMROSE.ACCOUNT_ADDRESS_TYPES)
      .on(PRIMROSE.ACCOUNT_ADDRESS_TYPES.ID.eq(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT_ADDRESS_TYPE))
      .where(
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT.eq(IdUtil.valueOfLongId(accountId)),
        PRIMROSE.ADDRESSES.ID.eq(IdUtil.valueOfLongId(addressId)),
        PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME.eq(type))
      .fetchOptional(record -> ImmutableAddress.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

  public Map<String, List<Address>> loadByAccountId(final String accountId) {
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
      .where(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT.eq(IdUtil.valueOfLongId(accountId)))
      .fetchGroups(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME, record -> ImmutableAddress.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());
  }

  public int count() {
    return create
      .fetchCount(PRIMROSE.ADDRESSES);
  }

  public List<Address> load(final Integer page, final Integer size, final Sort sort) {
    final int offset = page != null && size != null
      ? (page - 1) * size
      : 0;

    final int limit = size != null
      ? size
      : Integer.MAX_VALUE;

    return create
      .selectDistinct(
        PRIMROSE.ADDRESSES.ID,
        PRIMROSE.ADDRESSES.STREET,
        PRIMROSE.ADDRESSES.STREET_NUMBER,
        PRIMROSE.ADDRESSES.CITY,
        PRIMROSE.ADDRESSES.POSTAL_CODE,
        PRIMROSE.ADDRESSES.STATE,
        PRIMROSE.ADDRESSES.COUNTRY)
      .from(PRIMROSE.ADDRESSES)
      .orderBy(QueryUtil.map(sort, field -> {
        switch (field) {
          case "street":
            return PRIMROSE.ADDRESSES.STREET;
          case "streetNumber":
            return PRIMROSE.ADDRESSES.STREET_NUMBER;
          case "postalCode":
            return PRIMROSE.ADDRESSES.POSTAL_CODE;
          case "state":
            return PRIMROSE.ADDRESSES.STATE;
          case "country":
            return PRIMROSE.ADDRESSES.COUNTRY;
          case "city":
            return PRIMROSE.ADDRESSES.CITY;
          default:
            return null;
        }
      }))
      .offset(offset)
      .limit(limit)
      .fetch(record -> ImmutableAddress.builder()
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
