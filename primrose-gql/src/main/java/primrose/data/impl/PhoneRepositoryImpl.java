package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.PhoneRepository;
import primrose.jooq.Tables;
import primrose.service.Phone;

@Repository
public class PhoneRepositoryImpl implements PhoneRepository {
  private DSLContext create;

  public PhoneRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long save(String phone) {
    Long id = create
      .select(Tables.PHONE_NUMBERS.ID)
      .from(Tables.PHONE_NUMBERS)
      .where(Tables.PHONE_NUMBERS.PHONE.likeIgnoreCase(phone))
      .fetchOne(0, Long.class);

    if (id == null) {
      id = create
        .insertInto(Tables.PHONE_NUMBERS)
        .columns(Tables.PHONE_NUMBERS.PHONE)
        .values(phone)
        .returning(Tables.PHONE_NUMBERS.ID)
        .fetchOne()
        .getId();
    }

    return id;
  }

  @Override
  public void assignToCustomer(long customerId, long phoneId, String phoneType, Boolean primary) {
    create
      .insertInto(Tables.CUSTOMER_PHONE_NUMBERS)
      .columns(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .values(
        DSL.value(customerId),
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
  public List<Phone> customerPhones(long customerId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
      .leftJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerId))
      .fetch()
      .map(record -> new Phone(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<Phone> contactPhones(long contactId) {
    return create
      .select(
        Tables.PHONE_NUMBERS.ID,
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
      .leftJoin(Tables.PHONE_NUMBER_TYPES)
      .on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId))
      .fetch()
      .map(record -> new Phone(record.value1(), record.value2(), record.value3(), record.value4()));
  }

}
