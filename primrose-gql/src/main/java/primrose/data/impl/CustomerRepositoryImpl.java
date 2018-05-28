package primrose.data.impl;

import static org.jooq.impl.DSL.currentOffsetDateTime;
import static org.jooq.impl.DSL.value;
import static primrose.jooq.JooqUtil.search;
import static primrose.jooq.Tables.CONTACTS;
import static primrose.jooq.Tables.CUSTOMERS;
import static primrose.jooq.Tables.CUSTOMER_EMAILS;
import static primrose.jooq.Tables.CUSTOMER_PHONE_NUMBERS;
import static primrose.jooq.Tables.CUSTOMER_RELATION_TYPES;
import static primrose.jooq.Tables.CUSTOMER_TYPES;
import static primrose.jooq.Tables.EMAIL_TYPES;
import static primrose.jooq.Tables.PHONE_NUMBER_TYPES;

import java.util.List;
import java.util.stream.Collector;

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
import primrose.service.Email;
import primrose.service.Pagination;
import primrose.service.PhoneNumber;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

  private DSLContext create;

  public CustomerRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public CustomerCode create(Customer customer) {
    Select<Record1<Long>> type = create
      .select(CUSTOMER_TYPES.ID)
      .from(CUSTOMER_TYPES)
      .where(CUSTOMER_TYPES.CODE.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(CUSTOMER_RELATION_TYPES.ID)
      .from(CUSTOMER_RELATION_TYPES)
      .where(CUSTOMER_RELATION_TYPES.CODE.eq(customer.getRelationType()));

    CustomersRecord record = create
      .insertInto(CUSTOMERS)
      .columns(
        CUSTOMERS.CUSTOMER_TYPE,
        CUSTOMERS.CUSTOMER_RELATION_TYPE,
        CUSTOMERS.FULL_NAME,
        CUSTOMERS.DISPLAY_NAME,
        CUSTOMERS.DESCRIPTION,
        CONTACTS.CREATED_BY,
        CONTACTS.CREATED_AT,
        CONTACTS.CHANGED_BY,
        CONTACTS.CHANGED_AT)
      .values(
        type.asField(),
        relationType.asField(),
        value(customer.getFullName()),
        value(customer.getDisplayName()),
        value(customer.getDescription()),
        value("test"),
        currentOffsetDateTime(),
        value("test"),
        currentOffsetDateTime())
      .returning(CUSTOMERS.CODE, CUSTOMERS.ID)
      .fetchOne();

    customer.getEmails().forEach(email -> {
      create
        .insertInto(CUSTOMER_EMAILS)
        .columns(
          CUSTOMER_EMAILS.CUSTOMER,
          CUSTOMER_EMAILS.EMAIL,
          CUSTOMER_EMAILS.EMAIL_TYPE)
        .values(
          value(record.getId()),
          value(email.getValue()),
          create.select(EMAIL_TYPES.ID).from(EMAIL_TYPES).where(EMAIL_TYPES.CODE.eq(email.getType())).asField())
        .execute();
    });

    customer.getPhoneNumbers().forEach(phone -> {
      create
        .insertInto(CUSTOMER_PHONE_NUMBERS)
        .columns(
          CUSTOMER_PHONE_NUMBERS.CUSTOMER,
          CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER,
          CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE)
        .values(
          value(record.getId()),
          value(phone.getValue()),
          create.select(PHONE_NUMBER_TYPES.ID).from(PHONE_NUMBER_TYPES).where(PHONE_NUMBER_TYPES.CODE.eq(phone.getType())).asField())
        .execute();
    });

    return CustomerCode.of(record.getCode());
  }

  @Override
  public CustomerCode update(Customer customer) {
    Long customerId = create
      .select(CUSTOMERS.ID)
      .from(CUSTOMERS)
      .where(CUSTOMERS.CODE.eq(customer.getCode().getCode()))
      .forUpdate()
      .fetchOne(CUSTOMERS.ID);

    if (customerId == null) {
      // throw exception unknown customer
      throw new RuntimeException();
    }

    Select<Record1<Long>> type = create
      .select(CUSTOMER_TYPES.ID)
      .from(CUSTOMER_TYPES)
      .where(CUSTOMER_TYPES.CODE.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(CUSTOMER_RELATION_TYPES.ID)
      .from(CUSTOMER_RELATION_TYPES)
      .where(CUSTOMER_RELATION_TYPES.CODE.eq(customer.getRelationType()));

    int updated = create
      .update(CUSTOMERS)
      .set(CUSTOMERS.CUSTOMER_TYPE, type)
      .set(CUSTOMERS.CUSTOMER_RELATION_TYPE, relationType)
      .set(CUSTOMERS.FULL_NAME, customer.getFullName())
      .set(CUSTOMERS.DISPLAY_NAME, customer.getDisplayName())
      .set(CUSTOMERS.DESCRIPTION, customer.getDescription())
      .set(CUSTOMERS.CHANGED_BY, "test")
      .set(CUSTOMERS.CHANGED_AT, currentOffsetDateTime())
      .where(
        CUSTOMERS.CODE.eq(customer.getCode().getCode()),
        CUSTOMERS.CHANGED_AT.eq(customer.getVersion()))
      .execute();

    create.deleteFrom(CUSTOMER_EMAILS).where(CUSTOMER_EMAILS.CUSTOMER.eq(customerId)).execute();
    create.deleteFrom(CUSTOMER_PHONE_NUMBERS).where(CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerId)).execute();

    customer.getEmails().forEach(email -> {
      create
        .insertInto(CUSTOMER_EMAILS)
        .columns(
          CUSTOMER_EMAILS.CUSTOMER,
          CUSTOMER_EMAILS.EMAIL,
          CUSTOMER_EMAILS.EMAIL_TYPE)
        .values(
          value(customerId),
          value(email.getValue()),
          create.select(EMAIL_TYPES.ID).from(EMAIL_TYPES).where(EMAIL_TYPES.CODE.eq(email.getType())).asField())
        .execute();
    });

    customer.getPhoneNumbers().forEach(phone -> {
      create
        .insertInto(CUSTOMER_PHONE_NUMBERS)
        .columns(
          CUSTOMER_PHONE_NUMBERS.CUSTOMER,
          CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER,
          CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE)
        .values(
          value(customerId),
          value(phone.getValue()),
          create.select(PHONE_NUMBER_TYPES.ID).from(PHONE_NUMBER_TYPES).where(PHONE_NUMBER_TYPES.CODE.eq(phone.getType())).asField())
        .execute();
    });

    if (updated != 1) {
      // Throw error
    }

    return customer.getCode();
  }

  @Override
  public ImmutableList<CustomerPreview> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(CUSTOMER_EMAILS.EMAIL)
      .from(CUSTOMER_EMAILS)
      .where(CUSTOMER_EMAILS.CUSTOMER.eq(CUSTOMERS.ID))
      .orderBy(CUSTOMER_EMAILS.EMAIL)
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER)
      .from(CUSTOMER_PHONE_NUMBERS)
      .where(CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(CUSTOMERS.ID))
      .orderBy(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER)
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        CUSTOMERS.CODE,
        CUSTOMER_TYPES.CODE,
        CUSTOMER_RELATION_TYPES.CODE,
        CUSTOMERS.DISPLAY_NAME,
        CUSTOMERS.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> CustomerPreview.builder()
        .code(record.get(Tables.CUSTOMERS.CODE))
        .type(record.get(Tables.CUSTOMER_TYPES.CODE))
        .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.CODE))
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
      .from(CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(CUSTOMER_EMAILS)
      .where(
        search(pagination.getSearch(), CUSTOMER_EMAILS.EMAIL),
        CUSTOMERS.ID.eq(CUSTOMER_EMAILS.CUSTOMER));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(CUSTOMER_PHONE_NUMBERS)
      .where(
        search(pagination.getSearch(), CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER),
        CUSTOMERS.ID.eq(CUSTOMER_PHONE_NUMBERS.CUSTOMER));

    List<Condition> conditions = JooqUtil.search(
      pagination.getSearch(),
      CUSTOMERS.CODE,
      CUSTOMER_TYPES.CODE,
      CUSTOMER_TYPES.DEFAULT_NAME,
      CUSTOMER_RELATION_TYPES.CODE,
      CUSTOMER_RELATION_TYPES.DEFAULT_NAME,
      CUSTOMERS.DISPLAY_NAME,
      CUSTOMERS.FULL_NAME,
      CUSTOMERS.DESCRIPTION);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public Customer get(CustomerCode code) {
    Long customerId = create
      .select(Tables.CUSTOMERS.ID)
      .from(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.eq(code.getCode()))
      .fetchOne(Tables.CUSTOMERS.ID);

    if (customerId == null) {
      // throw exception unknown customer
      throw new RuntimeException();
    }

    return create
      .select(
        CUSTOMERS.CODE,
        CUSTOMER_TYPES.CODE,
        CUSTOMER_RELATION_TYPES.CODE,
        CUSTOMERS.DISPLAY_NAME,
        CUSTOMERS.FULL_NAME,
        CUSTOMERS.DESCRIPTION,
        CUSTOMERS.CHANGED_AT)
      .from(Tables.CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMERS.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(CUSTOMERS.CODE.eq(code.getCode()))
      .fetchOne()
      .map(record -> Customer.builder()
        .code(CustomerCode.of(record.get(CUSTOMERS.CODE)))
        .type(record.get(CUSTOMER_TYPES.CODE))
        .relationType(record.get(CUSTOMER_RELATION_TYPES.CODE))
        .displayName(record.get(CUSTOMERS.DISPLAY_NAME))
        .fullName(record.get(CUSTOMERS.FULL_NAME))
        .description(record.get(CUSTOMERS.DESCRIPTION))
        .version(record.get(CUSTOMERS.CHANGED_AT))
        .emails(listEmails(customerId, ImmutableList.toImmutableList()))
        .phoneNumbers(listPhoneNumbers(customerId, ImmutableList.toImmutableList()))
        .build());
  }

  private <C> C listEmails(long customerId, Collector<Email, ?, C> collector) {
    return create
      .select(
        EMAIL_TYPES.CODE,
        CUSTOMER_EMAILS.EMAIL)
      .from(CUSTOMER_EMAILS)
      .innerJoin(EMAIL_TYPES).on(EMAIL_TYPES.ID.eq(CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(
        CUSTOMER_EMAILS.CUSTOMER.eq(customerId))
      .fetch()
      .stream()
      .map(record -> Email.builder()
        .type(record.get(EMAIL_TYPES.CODE))
        .value(record.get(CUSTOMER_EMAILS.EMAIL))
        .build())
      .collect(collector);
  }

  private <C> C listPhoneNumbers(long customerId, Collector<PhoneNumber, ?, C> collector) {
    return create
      .select(
        PHONE_NUMBER_TYPES.CODE,
        CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER)
      .from(CUSTOMER_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBER_TYPES).on(PHONE_NUMBER_TYPES.ID.eq(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerId))
      .fetch()
      .stream()
      .map(record -> PhoneNumber.builder()
        .type(record.get(PHONE_NUMBER_TYPES.CODE))
        .value(record.get(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER))
        .build())
      .collect(collector);
  }
}
