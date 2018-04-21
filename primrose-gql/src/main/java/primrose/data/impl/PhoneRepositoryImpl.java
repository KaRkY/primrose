package primrose.data.impl;

import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import primrose.data.PhoneRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
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
  public void assignToCustomer(CodeId code, long phoneId, String phoneType, Boolean primary) {
    create
        .insertInto(Tables.CUSTOMER_PHONE_NUMBERS)
        .columns(
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER,
            Tables.CUSTOMER_PHONE_NUMBERS.PHONE,
            Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
            Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
        .values(
            DSL.value(code.getId()),
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
  public void assignToContact(CodeId code, long phoneId, String phoneType, Boolean primary) {
    create
        .insertInto(Tables.CONTACT_PHONE_NUMBERS)
        .columns(
            Tables.CONTACT_PHONE_NUMBERS.CONTACT,
            Tables.CONTACT_PHONE_NUMBERS.PHONE,
            Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
            Tables.CONTACT_PHONE_NUMBERS.PRIM)
        .values(
            DSL.value(code.getId()),
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
  public ImmutableList<PhoneFullDisplay> customerPhones(CodeId code) {
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
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM,
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO))
        .fetch()
        .stream()
        .map(record -> PhoneFullDisplay.builder()
            .id(record.get(Tables.PHONE_NUMBERS.ID))
            .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
            .value(record.get(Tables.PHONE_NUMBERS.PHONE))
            .primary(record.get(Tables.CUSTOMER_PHONE_NUMBERS.PRIM))
            .build())
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<PhoneFullDisplay> customerPhonesForUpdate(CodeId code) {
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
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM,
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO))
        .forUpdate()
        .fetch()
        .stream()
        .map(record -> PhoneFullDisplay.builder()
            .id(record.get(Tables.PHONE_NUMBERS.ID))
            .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
            .value(record.get(Tables.PHONE_NUMBERS.PHONE))
            .primary(record.get(Tables.CUSTOMER_PHONE_NUMBERS.PRIM))
            .build())
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<PhoneFullDisplay> contactPhones(CodeId code) {
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
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM,
                Tables.CONTACT_PHONE_NUMBERS.VALID_TO))
        .fetch()
        .stream()
        .map(record -> PhoneFullDisplay.builder()
            .id(record.get(Tables.PHONE_NUMBERS.ID))
            .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
            .value(record.get(Tables.PHONE_NUMBERS.PHONE))
            .primary(record.get(Tables.CONTACT_PHONE_NUMBERS.PRIM))
            .build())
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<PhoneFullDisplay> contactPhonesForUpdate(CodeId code) {
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
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM,
                Tables.CONTACT_PHONE_NUMBERS.VALID_TO))
        .forUpdate()
        .fetch()
        .stream()
        .map(record -> PhoneFullDisplay.builder()
            .id(record.get(Tables.PHONE_NUMBERS.ID))
            .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
            .value(record.get(Tables.PHONE_NUMBERS.PHONE))
            .primary(record.get(Tables.CONTACT_PHONE_NUMBERS.PRIM))
            .build())
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public boolean isAssignedToCustomer(CodeId code, long phoneId) {
    return create
        .selectOne()
        .from(Tables.CUSTOMER_PHONE_NUMBERS)
        .where(
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(phoneId),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM,
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO))
        .fetchOne(0, Integer.class) != null;
  }

  @Override
  public boolean isAssignedToContact(CodeId code, long phoneId) {
    return create
        .selectOne()
        .from(Tables.CONTACT_PHONE_NUMBERS)
        .where(
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(code.getId()),
            Tables.CONTACT_PHONE_NUMBERS.PHONE.eq(phoneId),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM,
                Tables.CONTACT_PHONE_NUMBERS.VALID_TO))
        .fetchOne(0, Integer.class) != null;
  }

  @Override
  public void removeFromCustomer(CodeId code, long phoneId) {
    create
        .update(Tables.CUSTOMER_PHONE_NUMBERS)
        .set(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(phoneId),
            Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO.isNull())
        .execute();
  }

  @Override
  public void removeExceptFromCustomer(CodeId code, Set<Long> phoneIds) {
    create
        .update(Tables.CUSTOMER_PHONE_NUMBERS)
        .set(Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_PHONE_NUMBERS.PHONE.notIn(phoneIds),
            Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO.isNull())
        .execute();
  }

  @Override
  public void removeFromContact(CodeId code, long phoneId) {
    create
        .update(Tables.CONTACT_PHONE_NUMBERS)
        .set(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(code.getId()),
            Tables.CONTACT_PHONE_NUMBERS.PHONE.eq(phoneId),
            Tables.CONTACT_PHONE_NUMBERS.VALID_TO.isNull())
        .execute();
  }

  @Override
  public void removeExceptFromContact(CodeId code, Set<Long> phoneIds) {
    create
        .update(Tables.CONTACT_PHONE_NUMBERS)
        .set(Tables.CONTACT_PHONE_NUMBERS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(code.getId()),
            Tables.CONTACT_PHONE_NUMBERS.PHONE.notIn(phoneIds),
            Tables.CONTACT_PHONE_NUMBERS.VALID_TO.isNull())
        .execute();
  }
}
