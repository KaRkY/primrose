package primrose.data.jooq;

import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.value;
import static pimrose.data.jooq.Primrose.PRIMROSE;
import static pimrose.data.jooq.Sequences.ADDRESSES_SEQ;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.data.AddressesRepository;
import primrose.model.input.BaseInputAddress;
import primrose.model.output.BaseOutputAccountAddress;
import primrose.util.IdUtil;

@Repository
public class JooqAddressesRepository implements AddressesRepository {

  private final DSLContext create;

  public JooqAddressesRepository(final DSLContext create) {
    this.create = create;
  }

  /* (non-Javadoc)
   * @see primrose.data.jooq.AddressesRepository#count()
   */
  @Override
  public int count() {
    return create
      .fetchCount(PRIMROSE.ADDRESSES);
  }

  /* (non-Javadoc)
   * @see primrose.data.jooq.AddressesRepository#insert(java.lang.String, primrose.model.input.BaseInputAddress, java.lang.String)
   */
  @Override
  public void insert(
    final String addressId,
    final BaseInputAddress address,
    final String user) {
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
        value(IdUtil.valueOfLongId(addressId)),
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

  /* (non-Javadoc)
   * @see primrose.data.jooq.AddressesRepository#loadByAccountId(java.util.List)
   */
  @Override
  public List<List<BaseOutputAccountAddress>> loadByAccountId(final List<String> accountId) {
    final Map<Long, List<BaseOutputAccountAddress>> groupedAddresses = create
      .select(
        PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT,
        PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME,
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
      .where(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT
        .in(accountId.stream().map(IdUtil::valueOfLongId).collect(Collectors.toList())))
      .fetchGroups(PRIMROSE.ACCOUNT_ADDRESSES.ACCOUNT, record -> ImmutableOutputAccountAddress.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.ADDRESSES.ID)))
        .type(record.getValue(PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME))
        .street(record.getValue(PRIMROSE.ADDRESSES.STREET))
        .streetNumber(record.getValue(PRIMROSE.ADDRESSES.STREET_NUMBER))
        .city(record.getValue(PRIMROSE.ADDRESSES.CITY))
        .postalCode(record.getValue(PRIMROSE.ADDRESSES.POSTAL_CODE))
        .state(record.getValue(PRIMROSE.ADDRESSES.STATE))
        .country(record.getValue(PRIMROSE.ADDRESSES.COUNTRY))
        .build());

    return accountId
      .stream()
      .map(IdUtil::valueOfLongId)
      .map(groupedAddresses::get)
      .map(addresses -> addresses != null ? addresses : Collections.<BaseOutputAccountAddress>emptyList())
      .collect(Collectors.toList());
  }

  /* (non-Javadoc)
   * @see primrose.data.jooq.AddressesRepository#nextValAddresses()
   */
  @Override
  public String nextValAddresses() {
    return IdUtil.toStringId(create
      .select(ADDRESSES_SEQ.nextval())
      .fetchOne()
      .value1());
  }

  /* (non-Javadoc)
   * @see primrose.data.jooq.AddressesRepository#update(java.lang.String, primrose.model.input.BaseInputAddress, java.lang.String)
   */
  @Override
  public void update(final String addressId, final BaseInputAddress address, final String user) {
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

}
