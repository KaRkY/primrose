package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.springframework.stereotype.Repository;

import primrose.data.MetaRepository;
import primrose.jooq.Tables;
import primrose.service.MetaType;

@Repository
public class MetaRepositoryImpl implements MetaRepository {

  private DSLContext create;

  public MetaRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public List<MetaType> customer() {
    return create
      .select(Tables.CUSTOMER_TYPES.SLUG, Tables.CUSTOMER_TYPES.NAME)
      .from(Tables.CUSTOMER_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean customer(String customer) {
    return create
      .selectOne()
      .from(Tables.CUSTOMER_TYPES)
      .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> customerRelation() {
    return create
      .select(Tables.CUSTOMER_RELATION_TYPES.SLUG, Tables.CUSTOMER_RELATION_TYPES.NAME)
      .from(Tables.CUSTOMER_RELATION_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean customerRelation(String customerRelation) {
    return create
      .selectOne()
      .from(Tables.CUSTOMER_RELATION_TYPES)
      .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customerRelation))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> address() {
    return create
      .select(Tables.ADDRESS_TYPES.SLUG, Tables.ADDRESS_TYPES.NAME)
      .from(Tables.ADDRESS_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean address(String address) {
    return create
      .selectOne()
      .from(Tables.ADDRESS_TYPES)
      .where(Tables.ADDRESS_TYPES.SLUG.eq(address))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> phoneNumber() {
    return create
      .select(Tables.PHONE_NUMBER_TYPES.SLUG, Tables.PHONE_NUMBER_TYPES.NAME)
      .from(Tables.PHONE_NUMBER_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean phoneNumber(String phoneNumber) {
    return create
      .selectOne()
      .from(Tables.PHONE_NUMBER_TYPES)
      .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phoneNumber))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> email() {
    return create
      .select(Tables.EMAIL_TYPES.SLUG, Tables.EMAIL_TYPES.NAME)
      .from(Tables.EMAIL_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean email(String email) {
    return create
      .selectOne()
      .from(Tables.EMAIL_TYPES)
      .where(Tables.EMAIL_TYPES.SLUG.eq(email))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> contact() {
    return create
      .select(Tables.CONTACT_TYPES.SLUG, Tables.CONTACT_TYPES.NAME)
      .from(Tables.CONTACT_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean contact(String contact) {
    return create
      .selectOne()
      .from(Tables.CONTACT_TYPES)
      .where(Tables.CONTACT_TYPES.SLUG.eq(contact))
      .fetchOne(0) != null;
  }

  @Override
  public List<MetaType> meeting() {
    return create
      .select(Tables.MEETING_TYPES.SLUG, Tables.MEETING_TYPES.NAME)
      .from(Tables.MEETING_TYPES)
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean meeting(String meeting) {
    return create
      .selectOne()
      .from(Tables.MEETING_TYPES)
      .where(Tables.MEETING_TYPES.SLUG.eq(meeting))
      .fetchOne(0) != null;
  }

  private MetaType map(Record2<String, String> record) {
    return new MetaType(record.value1(), record.value2());
  }
}
