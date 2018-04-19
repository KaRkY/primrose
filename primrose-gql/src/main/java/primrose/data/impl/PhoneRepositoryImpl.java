package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.PhoneRepository;
import primrose.jooq.Tables;
import primrose.service.PhoneFullDisplay;

@Repository
public class PhoneRepositoryImpl implements PhoneRepository {
  private DSLContext create;

  public PhoneRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long save(String phone) {
    return create
      .insertInto(Tables.PHONE_NUMBERS)
      .columns(Tables.PHONE_NUMBERS.PHONE)
      .values(phone)
      .returning(Tables.PHONE_NUMBERS.ID)
      .fetchOne()
      .getId();
  }

  @Override
  public Long get(String phone) {
    return create
      .select(Tables.PHONE_NUMBERS.ID)
      .from(Tables.PHONE_NUMBERS)
      .where(Tables.PHONE_NUMBERS.PHONE.likeIgnoreCase(phone))
      .fetchOne(0, Long.class);
  }

  @Override
  public void assignToCustomer(long customerCodeId, long phoneId, String phoneType, Boolean primary) {
    create
      .insertInto(Tables.CUSTOMER_PHONE_NUMBERS)
      .columns(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .values(
        DSL.value(customerCodeId),
        DSL.value(phoneId),
        create
          .select(Tables.PHONE_NUMBER_TYPES.ID)
          .from(Tables.PHONE_NUMBER_TYPES)
          .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phoneType))
          .asField(),
        DSL.value(primary != null ? primary : false))
      .execute();
  }

  @Override
  public void assignToContact(long contactId, long phoneId, String phoneType, Boolean primary) {
    create
      .insertInto(Tables.CONTACT_PHONE_NUMBERS)
      .columns(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT,
        Tables.CONTACT_PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .values(
        DSL.value(contactId),
        DSL.value(phoneId),
        create
          .select(Tables.PHONE_NUMBER_TYPES.ID)
          .from(Tables.PHONE_NUMBER_TYPES)
          .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phoneType))
          .asField(),
        DSL.value(primary != null ? primary : false))
      .execute();
  }

  @Override
  public List<PhoneFullDisplay> customerPhones(long customerCodeId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
      .innerJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerCodeId),
        DSL.currentOffsetDateTime().between(Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetch()
      .map(record -> new PhoneFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<PhoneFullDisplay> customerPhonesForUpdate(long customerCodeId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
      .innerJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerCodeId),
        DSL.currentOffsetDateTime().between(Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .forUpdate()
      .fetch()
      .map(record -> new PhoneFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<PhoneFullDisplay> contactPhones(long contactId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
      .innerJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        DSL.currentOffsetDateTime().between(Tables.CONTACT_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetch()
      .map(record -> new PhoneFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<PhoneFullDisplay> contactPhonesForUpdate(long contactId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
      .innerJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        DSL.currentOffsetDateTime().between(Tables.CONTACT_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .forUpdate()
      .fetch()
      .map(record -> new PhoneFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public boolean isAssignedToCustomer(long customerCodeId, long phoneId) {
    return create
      .selectOne()
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerCodeId),
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(phoneId),
        DSL.currentOffsetDateTime()
          .between(Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetchOne(0, Integer.class) != null;
  }

  @Override
  public boolean isAssignedToContact(long contactId, long phoneId) {
    return create
      .selectOne()
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        Tables.CONTACT_PHONE_NUMBERS.PHONE.eq(phoneId),
        DSL.currentOffsetDateTime()
          .between(Tables.CONTACT_PHONE_NUMBERS.VALID_FROM)
          .and(DSL.isnull(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetchOne(0, Integer.class) != null;
  }

  @Override
  public void removeFromCustomer(long customerCodeId, long phoneId) {
    create
      .update(Tables.CUSTOMER_PHONE_NUMBERS)
      .set(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerCodeId),
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(phoneId),
        Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO.isNull())
      .execute();
  }

  @Override
  public void removeFromContact(long contactId, long phoneId) {
    create
      .update(Tables.CONTACT_PHONE_NUMBERS)
      .set(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        Tables.CONTACT_PHONE_NUMBERS.PHONE.eq(phoneId),
        Tables.CONTACT_PHONE_NUMBERS.VALID_TO.isNull())
      .execute();
  }

}
