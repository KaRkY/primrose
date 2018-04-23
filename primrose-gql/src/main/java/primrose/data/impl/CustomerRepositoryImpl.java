package primrose.data.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import primrose.data.CustomerRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.jooq.tables.records.CustomersRecord;
import primrose.service.CreateEmail;
import primrose.service.CreatePhone;
import primrose.service.EmailFullDisplay;
import primrose.service.Pagination;
import primrose.service.PhoneFullDisplay;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerEdit;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

  private DSLContext create;

  public CustomerRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public String create(CustomerCreate customer) {

    Select<Record1<Long>> type = create
      .select(Tables.CUSTOMER_TYPES.ID)
      .from(Tables.CUSTOMER_TYPES)
      .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(Tables.CUSTOMER_RELATION_TYPES.ID)
      .from(Tables.CUSTOMER_RELATION_TYPES)
      .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customer.getRelationType()));

    CustomersRecord customerRecord = create
      .insertInto(Tables.CUSTOMERS)
      .columns(
        Tables.CUSTOMERS.CUSTOMER_TYPE,
        Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE,
        Tables.CUSTOMERS.FULL_NAME,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.DESCRIPTION)
      .values(
        type.asField(),
        relationType.asField(),
        DSL.value(customer.getFullName()),
        DSL.value(customer.getDisplayName()),
        DSL.value(customer.getDescription()))
      .returning(
        Tables.CUSTOMERS.CODE,
        Tables.CUSTOMERS.ID)
      .fetchOne();

    customer.getEmails().forEach(email -> {
      assignEmail(customerRecord.getId(), email);
    });

    customer.getPhones().forEach(phone -> {
      assignPhone(customerRecord.getId(), phone);
    });

    return customerRecord.getCode();
  }

  @Override
  public void update(String code, CustomerEdit customer) {

    Long customerId = create
      .select(Tables.CUSTOMERS.ID)
      .from(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.eq(code))
      .fetchOne(Tables.CUSTOMERS.ID);

    Select<Record1<Long>> type = create
      .select(Tables.CUSTOMER_TYPES.ID)
      .from(Tables.CUSTOMER_TYPES)
      .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(Tables.CUSTOMER_RELATION_TYPES.ID)
      .from(Tables.CUSTOMER_RELATION_TYPES)
      .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customer.getRelationType()));

    create
      .update(Tables.CUSTOMERS)
      .set(Tables.CUSTOMERS.FULL_NAME, customer.getFullName())
      .set(Tables.CUSTOMERS.DISPLAY_NAME, customer.getDisplayName())
      .set(Tables.CUSTOMERS.DESCRIPTION, customer.getDescription())
      .set(Tables.CUSTOMERS.CUSTOMER_TYPE, type)
      .set(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE, relationType)
      .where(
        Tables.CUSTOMERS.ID.eq(customerId),
        Tables.CUSTOMERS.MODIFIED_AT.eq(customer.getVersion()));

    Set<Long> assignedEmails = new HashSet<>();
    customer.getEmails().forEach(email -> {
      Long id = assignedEmail(customerId, email);
      if (id == null) {
        id = assignEmail(customerId, email);
      }
      assignedEmails.add(id);
    });
    removeEmailsExcept(customerId, assignedEmails);

    Set<Long> assignedPhones = new HashSet<>();
    customer.getPhones().forEach(phone -> {
      Long id = assignedPhone(customerId, phone);
      if (id == null) {
        id = assignPhone(customerId, phone);
      }
      assignedPhones.add(id);
    });
    removePhoneNumbersExcept(customerId, assignedPhones);
  }

  private long assignEmail(long customer, CreateEmail email) {
    return create
      .insertInto(Tables.CUSTOMER_EMAILS)
      .columns(
        Tables.CUSTOMER_EMAILS.CUSTOMER,
        Tables.CUSTOMER_EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.EMAIL_TYPE,
        Tables.CUSTOMER_EMAILS.PRIM)
      .values(
        DSL.value(customer),
        DSL.value(email.getValue()),
        create
          .select(Tables.EMAIL_TYPES.ID)
          .from(Tables.EMAIL_TYPES)
          .where(Tables.EMAIL_TYPES.SLUG.eq(email.getType()))
          .asField(),
        DSL.value(email.getPrimary() != null ? email.getPrimary() : false))
      .returning(Tables.CUSTOMER_EMAILS.ID)
      .fetchOne()
      .get(Tables.CUSTOMER_EMAILS.ID);
  }

  private Long assignedEmail(long customer, CreateEmail email) {
    return create
      .select(Tables.CUSTOMER_EMAILS.ID)
      .from(Tables.CUSTOMER_EMAILS)
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customer),
        Tables.CUSTOMER_EMAILS.EMAIL_TYPE.eq(
          create
            .select(Tables.EMAIL_TYPES.ID)
            .from(Tables.EMAIL_TYPES)
            .where(Tables.EMAIL_TYPES.SLUG.eq(email.getType()))
            .asField()),
        Tables.CUSTOMER_EMAILS.EMAIL.likeIgnoreCase(email.getValue()))
      .forUpdate()
      .fetchOne(Tables.CUSTOMER_EMAILS.ID);
  }

  private ImmutableList<EmailFullDisplay> assignedEmail(long customer) {
    return create
      .select(
        Tables.EMAIL_TYPES.SLUG,
        Tables.CUSTOMER_EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.PRIM)
      .from(Tables.CUSTOMER_EMAILS)
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customer))
      .fetch()
      .stream()
      .map(record -> EmailFullDisplay.builder()
        .type(record.get(Tables.EMAIL_TYPES.SLUG))
        .value(record.get(Tables.CUSTOMER_EMAILS.EMAIL))
        .primary(record.get(Tables.CUSTOMER_EMAILS.PRIM))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  public void removeEmailsExcept(long customer, Set<Long> emailIds) {
    create
      .deleteFrom(Tables.CUSTOMER_EMAILS)
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customer),
        Tables.CUSTOMER_EMAILS.ID.notIn(emailIds))
      .execute();
  }

  private long assignPhone(long customer, CreatePhone phone) {
    return create
      .insertInto(Tables.CUSTOMER_PHONE_NUMBERS)
      .columns(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .values(
        DSL.value(customer),
        DSL.value(phone.getValue()),
        create
          .select(Tables.PHONE_NUMBER_TYPES.ID)
          .from(Tables.PHONE_NUMBER_TYPES)
          .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phone.getType()))
          .asField(),
        DSL.value(phone.getPrimary() != null ? phone.getPrimary() : false))
      .returning(Tables.CUSTOMER_PHONE_NUMBERS.ID)
      .fetchOne()
      .get(Tables.CUSTOMER_PHONE_NUMBERS.ID);
  }

  private Long assignedPhone(long customer, CreatePhone phone) {
    return create
      .select(Tables.CUSTOMER_PHONE_NUMBERS.ID)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customer),
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE.eq(
          create
            .select(Tables.PHONE_NUMBER_TYPES.ID)
            .from(Tables.PHONE_NUMBER_TYPES)
            .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phone.getType()))
            .asField()),
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE.likeIgnoreCase(phone.getValue()))
      .forUpdate()
      .fetchOne(Tables.CUSTOMER_PHONE_NUMBERS.ID);
  }

  private ImmutableList<PhoneFullDisplay> assignedPhone(long customer) {
    return create
      .select(
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.CUSTOMER_PHONE_NUMBERS.PHONE,
        Tables.CUSTOMER_PHONE_NUMBERS.PRIM)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBER_TYPES).on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customer))
      .fetch()
      .stream()
      .map(record -> PhoneFullDisplay.builder()
        .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
        .value(record.get(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
        .primary(record.get(Tables.CUSTOMER_PHONE_NUMBERS.PRIM))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  private void removePhoneNumbersExcept(long customer, Set<Long> phoneIds) {
    create
      .deleteFrom(Tables.CUSTOMER_PHONE_NUMBERS)
      .where(
        Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customer),
        Tables.CUSTOMER_PHONE_NUMBERS.ID.notIn(phoneIds))
      .execute();
  }

  @Override
  public ImmutableList<CustomerReducedDisplay> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(Tables.CUSTOMER_EMAILS.EMAIL)
      .from(Tables.CUSTOMER_EMAILS)
      .where(Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
      .orderBy(Tables.CUSTOMER_EMAILS.PRIM.desc())
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(Tables.CUSTOMER_PHONE_NUMBERS.PHONE)
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .where(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
      .orderBy(Tables.CUSTOMER_PHONE_NUMBERS.PRIM.desc())
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        Tables.CUSTOMERS.CODE,
        Tables.CUSTOMER_TYPES.SLUG,
        Tables.CUSTOMER_RELATION_TYPES.SLUG,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(Tables.CUSTOMERS)
      .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
      .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> CustomerReducedDisplay.builder()
        .code(record.get(Tables.CUSTOMERS.CODE))
        .type(record.get(Tables.CUSTOMER_TYPES.SLUG))
        .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.SLUG))
        .displayName(record.get(Tables.CUSTOMERS.DISPLAY_NAME))
        .fullName(record.get(Tables.CUSTOMERS.FULL_NAME))
        .primaryEmail(record.get(primaryEmail))
        .primaryPhone(record.get(primaryPhone))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  @Override
  public long count(Pagination pagination) {

    Condition searchCondition = buildSearchCondition(pagination);

    return create
      .selectCount()
      .from(Tables.CUSTOMERS)
      .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
      .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(Tables.CUSTOMER_EMAILS)
      .where(
        JooqUtil.search(pagination.getQuery(), Tables.CUSTOMER_EMAILS.EMAIL),
        Tables.CUSTOMERS.ID.eq(Tables.CUSTOMER_EMAILS.CUSTOMER));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(Tables.CUSTOMER_PHONE_NUMBERS)
      .where(
        JooqUtil.search(pagination.getQuery(), Tables.CUSTOMER_PHONE_NUMBERS.PHONE),
        Tables.CUSTOMERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER));

    List<Condition> conditions = JooqUtil.search(
      pagination.getQuery(),
      Tables.CUSTOMERS.CODE,
      Tables.CUSTOMER_TYPES.SLUG,
      Tables.CUSTOMER_TYPES.NAME,
      Tables.CUSTOMER_RELATION_TYPES.SLUG,
      Tables.CUSTOMER_RELATION_TYPES.NAME,
      Tables.CUSTOMERS.DISPLAY_NAME,
      Tables.CUSTOMERS.FULL_NAME);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public CustomerFullDisplay get(String code) {
    Long customerId = create
      .select(Tables.CUSTOMERS.ID)
      .from(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.eq(code))
      .fetchOne(Tables.CUSTOMERS.ID);

    return create
      .select(
        Tables.CUSTOMERS.CODE,
        Tables.CUSTOMER_TYPES.SLUG,
        Tables.CUSTOMER_RELATION_TYPES.SLUG,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.FULL_NAME,
        Tables.CUSTOMERS.DESCRIPTION,
        Tables.CUSTOMERS.MODIFIED_AT)
      .from(Tables.CUSTOMERS)
      .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
      .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(Tables.CUSTOMERS.CODE.eq(code))
      .fetchOne()
      .map(record -> CustomerFullDisplay.builder()
        .code(record.get(Tables.CUSTOMERS.CODE))
        .type(record.get(Tables.CUSTOMER_TYPES.SLUG))
        .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.SLUG))
        .displayName(record.get(Tables.CUSTOMERS.DISPLAY_NAME))
        .fullName(record.get(Tables.CUSTOMERS.FULL_NAME))
        .description(record.get(Tables.CUSTOMERS.DESCRIPTION))
        .version(record.get(Tables.CUSTOMERS.MODIFIED_AT))
        .emails(assignedEmail(customerId))
        .phones(assignedPhone(customerId))
        .build());
  }

  @Override
  public void delete(String code) {
    create
      .deleteFrom(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.eq(code))
      .execute();
  }

  @Override
  public void delete(Set<String> codes) {
    create
      .deleteFrom(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.in(codes))
      .execute();
  }
}
